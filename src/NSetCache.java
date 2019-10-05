package src;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.Method;
import java.security.*;
import java.math.BigInteger;

public class NSetCache<K,V> implements CacheLibrary<K,V>
{
  // data layer of Cache
  // HashMap: Integer => HashMap
  //                            K => V
  // private variables
  private final int N_sets;
  private final int M_blocks;
  private final String name;
  private Method replacementAlgorithm;
  private final HashMap<Integer, HashMap<K,CacheItem<K,V>>> cache = new HashMap<Integer, HashMap<K,CacheItem<K,V>>>();

  /**
   * @brief Basic Constructor
   * @param String name The name of the cache
   * @param Integer n The number of sets
   * @param Integer m The number of entries per set
   */
  public NSetCache(String name, Integer n, Integer m)
  {
    this.name = name;
    this.N_sets = n;
    this.M_blocks = m;
    // initialize n blocks
    for (int i = 0; i < n; i++)
    {
      this.cache.put(i, new HashMap<K,CacheItem<K,V>>(m));
    }
  }

  /**
   * @brief Constructor
   * @param String name The name of the cache
   * @param Integer n The number of sets
   * @param Integer m The number of entries per set
   * @param String function The name of the algorithm to invoke when the cache is full
   */
  public NSetCache(String name, Integer n, Integer m, String function)
  {
    this.name = name;
    this.N_sets = n;
    this.M_blocks = m;
    try
    {
      // @TODO throwing exception here for LRU
      Method method = this.getClass().getMethod(function, Integer.class);
      this.replacementAlgorithm = method;
    }
    catch (NoSuchMethodException e)
    {
      // stop object instantiation
      System.out.println("Invalid method " + function + " used as replacement algorithm.");
    }
    // initialize n blocks
    for (int i = 0; i < n; i++)
    {
      this.cache.put(i, new HashMap<K,CacheItem<K,V>>(m));
    }
  }

  public NSetCache(String name, Integer n, Integer m, String function, HashMap<Integer, HashMap<K,CacheItem<K,V>>> loadedMap)
  {
    this.name = name;
    this.N_sets = n;
    this.M_blocks = m;
    try
    {
      Method method = NSetCache.class.getDeclaredMethod(function, Integer.class);
      this.replacementAlgorithm = method;
    }
    catch (NoSuchMethodException e)
    {
      // stop object instantiation
      System.out.println("Invalid method used as replacement algorithm.");
    }
    // verify the loadedMap satisfies the N set conditions
  }

  /**
   * @brief Retrieves a value from the cache by a given key
   * @return V Value corresponding to given key
   */
  public CacheItem get(K key)
  {
    // get block to search
    int index = this.getHash(key);

    if (index >= N_sets || index < 0)
    {
      return retrieveFromDB(key);
    }

    System.out.println("GET INDEX: " + index);

    try
    {
      // get entry from block
      HashMap<K, CacheItem<K,V>> block = this.cache.get(index);
      CacheItem<K,V> entry = block.get(key);
      return entry;
    }
    catch (NullPointerException e)
    {
      System.out.println("No such value with the given key.");
      return retrieveFromDB(key);
    }
  }

  /**
   * @brief Inserts a key-value pair into the cache
   * @param K key
   * @param V value
   * @return boolean True if new pair, False if old pair
   */
  public boolean set(K key, V value)
  {
    CacheItem<K,V> entry = new CacheItem<K,V>(key, value);
    int index = this.getHash(key);
    System.out.println("SET INDEX: " + index);
    HashMap<K, CacheItem<K,V>> block = this.cache.get(index);
    if (block.size() > this.M_blocks)
    {
      eviction(index);
    }
    if (this.cache.get(index).put(key, entry) == null)
    {
      return true;
    }
    return false;
  }

  /**
   * @brief Deletes a key-value entry from the cache by a given key
   * @param K key
   * @return boolean True on success
   */
  public boolean delete(K key)
  {
    int index = this.getHash(key);
    System.out.println("DELETE INDEX: " + index);
    CacheItem prevEntry = this.cache.get(index).get(key);
    CacheItem deletedEntry = this.cache.get(index).remove(key);

    System.out.println("REMOVED: " + deletedEntry.getValue());
    System.out.println("ACTUAL: " + prevEntry.getValue());

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
      collection.put(keys[i], this.get(keys[i]));
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
    this.cache.clear();
  }

  /**
   * @brief Simple LRU Replacement Algorithm
   */
  public void LRU(int index)
  {
    CacheItem<K,V> least = new CacheItem<K,V>(null,null);
    for (Map.Entry<K, CacheItem<K,V>> entry : this.cache.get(index).entrySet())
    {
      CacheItem<K,V> curr = entry.getValue();
      if (curr.getTimestamp() < least.getTimestamp())
      {
        least = curr;
      }
    }
    this.cache.get(index).remove(least.getKey());
  }

  /**
   * @brief Simple MRU Replacement Algorithm
   */
  public void MRU(int index)
  {
    CacheItem<K,V> recent = new CacheItem<K,V>(null,null);

    for (Map.Entry<K, CacheItem<K,V>> entry : this.cache.get(index).entrySet())
    {
      CacheItem<K,V> curr = entry.getValue();
      if (curr.getTimestamp() < recent.getTimestamp())
      {
        recent = curr;
      }
    }
    this.cache.get(index).remove(recent.getKey());
  }

  /**
   * @brief Client usage function intended to be overriden with a custom replacement algorithmm
   * @param int Index The index of the block to evict an entry from
   */
  public void customReplacementAlgorithm(int index)
  {
    // calls default replacement algorithm LRU
    this.LRU(index);
  }

  /**
   * @brief Retrieves the hashCode based on the key provided
   * @param  K key [description]
   * @return Integer The index of the block in which this key is mapped to
   */
  public int getHash(K key)
  {
      int hash = Objects.hash(key);
      String hashString = Integer.toString(hash);

      try {
        MessageDigest md = MessageDigest.getInstance("MD5");
  	    byte[] messageDigest = md.digest(hashString.getBytes());

        // byte array to hex value
        BigInteger hex = new BigInteger(1, messageDigest);

        // hex value to long integer
        BigInteger value = new BigInteger(hex.toString(16), 16);

        // mod to retrieve an index from 0 to N_sets - 1 buckets
        int hashValue = value.intValue() % this.N_sets;

        // prevent negative numbers
        if (hashValue < 0)
        {
          hashValue = hashValue * -1;
        }
        return hashValue;
      }
	    catch (NoSuchAlgorithmException e)
	    {
        // log an error
          return 0;
	    }
  }

  /**
   * @brief Evicts key-value pairs when cache is full and an insertion is required
   */
  public void eviction(int index)
  {
    try
    {
      // this.setReplacementAlgorithm("tesst"); // test, can be removed
      this.replacementAlgorithm.invoke(this, index);
    }
    catch (Exception e)
    {
      System.out.println("Replacement algorithm not declared in cache object's class.");
    }
  }

  private CacheItem retrieveFromDB(K key)
  {
    CacheItem<K,V> dummyItem = new CacheItem<K,V>(null,null);
    return dummyItem;
  }
}
