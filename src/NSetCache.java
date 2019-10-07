import java.io.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.Method;
import java.security.*;
import java.math.BigInteger;

public class NSetCache<K,V> implements CacheLibrary<K,V>
{
  public static final String LEAST_RECENT = "LRU";
  public static final String MOST_RECENT = "MRU";
  public static final String USER_DEFINED = "USER DEFINED";

  // private variables
  private final int N_sets;
  private final int M_blocks;
  private final String name;
  private final String method;
  private final HashMap<Integer, HashMap<K,CacheItem>> cache = new HashMap<Integer, HashMap<K,CacheItem>>();

  /**
   * @brief Basic Constructor
   * @param String name The name of the cache
   * @param Integer n The number of sets
   * @param Integer m The number of entries per set
   */
  public NSetCache(String name, Integer n, Integer m) throws IllegalArgumentException
  {
    this(name, n, m, NSetCache.LEAST_RECENT);
  }

  /**
   * @brief Constructor
   * @param String name The name of the cache
   * @param Integer n The number of sets
   * @param Integer m The number of entries per set
   * @param String function The name of the algorithm to invoke when the cache is full
   */
  public NSetCache(String name, Integer n, Integer m, String function) throws IllegalArgumentException
  {
    if (n <= 0 || m <= 0)
    {
      throw new IllegalArgumentException("Invalid number of sets or entries per set");
    }
    if (function != NSetCache.MOST_RECENT && function != NSetCache.LEAST_RECENT && function != NSetCache.USER_DEFINED)
    {
      throw new IllegalArgumentException("Invalid replacement algorithm specified.");
    }
    this.name = name;
    this.N_sets = n;
    this.M_blocks = m;
    this.method = function;

    // initialize n blocks
    for (int i = 0; i < n; i++)
    {
      this.cache.put(i, new HashMap<K,CacheItem>(m));
    }
  }

  /**
   * @brief Retrieves a value from the cache by a given key
   * @return V Value corresponding to given key NULL if no entry in cache
   */
  public CacheItem get(K key)
  {
    // get block index to search
    int index = this.getHash(key);

    // get block
    HashMap<K, CacheItem> block = this.cache.get(index);

    // get entry from block
    CacheItem entry = block.get(key);

    // if no entry corresponds to given key
    if (entry == null)
    {
      return null;
    }
    // update timestamp
    this.cache.get(index).get(key).updateTimestamp();
    return entry;
  }

  /**
   * @brief Inserts a key-value pair into the cache
   * @param K key
   * @param V value
   * @return boolean True if new pair, False if old pair
   */
  public boolean set(K key, V value)
  {
    CacheItem entry = new CacheItem(key, value);
    int index = this.getHash(key);
    HashMap<K, CacheItem> block = this.cache.get(index);

    if (block.size() == this.M_blocks)
    {
      eviction(index);
    }
    if (this.cache.get(index).put(key, entry) == null)
    {
      // new key-value pair
      return true;
    }
    return false;
    // else, it is existing key, new CacheItem entry created w/ latest timestamp replaces it
  }

  /**
   * @brief Deletes a key-value entry from the cache by a given key
   * @param K key
   * @return boolean True on success
   */
  public boolean delete(K key)
  {
    int index = this.getHash(key);
    CacheItem prevEntry = this.cache.get(index).get(key);
    CacheItem deletedEntry = this.cache.get(index).remove(key);

    if (deletedEntry.getValue() == prevEntry.getValue())
    {
      return true;
    }
    return false;
  }
 /**
  * @brief Retrieves key-value pairs from the cache by the given keys
  * @return HashMap<K,V> collection
  */
  public HashMap<K,CacheItem> getCollection(K[] keys)
  {
    HashMap<K,CacheItem> collection = new HashMap<K,CacheItem>();
    for (int i = 0; i < keys.length; i++)
    {
      CacheItem curr = this.get(keys[i]);
      if (curr != null)
      {
        collection.put(keys[i], curr);
      }
    }
    return collection;
  }

 /**
  * @brief Inserts key-value pairs into the cache
  * @param HashMap<K,V> entries
  */
  public boolean setCollection(HashMap<K, V> entries)
  {
    for (Map.Entry<K,V> entry : entries.entrySet())
    {
      this.set(entry.getKey(), entry.getValue());
    }
    return true;
  }

  /**
   * @brief Deletes key-value entries from the cache that corresponds to given keys
   * @param K key
   * @return boolean True on success
   */
  public boolean deleteCollection(K keys[])
  {
    for (int i = 0; i < keys.length; i++)
    {
      this.delete(keys[i]);
    }
    return true;
  }

  /**
   * @brief Clears all entries from the cache
   */
  public void clear()
  {
    for (int i = 0; i < N_sets; i++)
    {
      // clear blocks in sets
      this.cache.get(i).clear();
    }
  }

  /**
   * @brief Simple LRU Replacement Algorithm
   */
  public void LRU(int index)
  {
    CacheItem least = new CacheItem(null,null);
    for (Map.Entry<K, CacheItem> entry : this.cache.get(index).entrySet())
    {
      CacheItem curr = entry.getValue();
      if (curr.getTimestamp() < least.getTimestamp())
      {
        least = curr;
      }
    }
    this.delete((K)least.getKey());
  }

  /**
   * @brief Simple MRU Replacement Algorithm
   */
  public void MRU(int index)
  {
    CacheItem recent = new CacheItem(null,null);
    boolean first = false;

    for (Map.Entry<K, CacheItem> entry : this.cache.get(index).entrySet())
    {
      CacheItem curr = entry.getValue();
      if (first == false)
      {
        recent = curr;
        first = true;
      }
      else if (curr.getTimestamp() > recent.getTimestamp())
      {
        recent = curr;
      }
    }
    this.delete((K)recent.getKey());
  }

  /**
   * @brief Client usage function intended to be overriden with a custom replacement algorithmm
   * @param int Index The index of the block to evict an entry from
   */
  public void userDefinedReplacementAlgorithm(int index)
  {
    // calls default replacement algorithm LRU
    this.LRU(index);
  }

  /**
   * @brief Retrieves the hashCode based on the key provided
   * @Note hashCode and equals function must be overridden for user created objects to ensure equality comparison for
   * different object instantiations with same member variables. Otherwise, getHash may differ for supposedly "equal" objects
   * @param  K key [description]
   * @return Integer The index of the block in which this key is mapped to
   */
  public int getHash(K key)
  {
      // get object's hashCode
      int hash = key.hashCode();
      // convert to a string for MD5
      String hashString = Objects.toString(key);

      try
      {
        MessageDigest md = MessageDigest.getInstance("MD5");
  	byte[] messageDigest = md.digest(hashString.getBytes());

        // byte array to hex value (1 indicates positive)
        BigInteger hex = new BigInteger(1, messageDigest);

        // hex value to long integer
        BigInteger value = new BigInteger(hex.toString(16), 16);

        // mod to retrieve an index from 0 to N_sets - 1 buckets
        int hashValue = value.intValue() % this.N_sets;

        // prevent negative numbers (shouldn't happen)
        if (hashValue < 0)
        {
          hashValue = hashValue * -1;
        }
        return hashValue;
      }
      catch (NoSuchAlgorithmException e)
      {
        // log an error
        e.printStackTrace();
        return 0;
      }
  }

  /**
   * @brief Evicts key-value pairs when cache is full and an insertion is required
   */
  public void eviction(int index)
  {
    switch (this.method)
    {
      case NSetCache.LEAST_RECENT:
        this.LRU(index);
        break;
      case NSetCache.MOST_RECENT:
        this.MRU(index);
        break;
      case NSetCache.USER_DEFINED:
        this.userDefinedReplacementAlgorithm(index);
        break;
      default:
        this.LRU(index);
    }
  }
}
