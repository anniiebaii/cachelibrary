package src;
import java.io.*;
import java.util.*;
import java.lang.*;

public interface NSetCache<K,V> implements CacheLibrary<K,V>
{
  // data layer of Cache
  // HashMap: Integer => HashMap
  //                            K => V
  // private variables
  private final int N;
  private final String name;
  private final HashMap<Integer, HashMap<K,CacheItem>> cache = new HashMap<Integer, HashMap<K,CacheItem>>();

  /**
   * @brief Basic Constructor
   * @param String name The name of the cache
   * @param Integer n The number of sets and entries in each set
   */
  public NSetCache(String name, Integer n)
  {
    this.name = name;
    this.N = n;
  }

  public NSetCache(String name, Integer n, HashMap<Integer, HashMap<K,CacheItem>> loadedMap)
  {
    this.name = name;
    this.N = n;

    // verify the loadedMap satisfies the N set conditions
  }

  /**
   * @brief Retrieves a value from the cache by a given key
   * @return V Value corresponding to given key
   */
  public V get(K key)
  {}

  /**
   * @brief Inserts a key-value pair into the cache
   * @param K key
   * @param V value
   * @return boolean True on success
   */
  public boolean set(K key, V value)
  {}

  /**
   * @brief Deletes a key-value entry from the cache by a given key
   * @param K key
   * @return boolean True on success
   */
  public boolean delete(K key)
  {
    return false;
  }
 /**
  * @brief Retrieves key-value pairs from the cache by the given keys
  * @return HashMap<K,V> collection
  */
  public HashMap<K,V> getCollection(K[] keys)
  {}

 /**
  * @brief Inserts key-value pairs into the cache
  * @param HashMap<K,V> entries
  */
  public boolean setCollection(HashMap<K, V> entries)
  {
    return false;
  }

  /**
   * @brief Deletes key-value entries from the cache that corresponds to given keys
   * @param K key
   * @return boolean True on success
   */
  public boolean deleteCollection(K keys[])
  {
    return false;
  }

  /**
   * @brief Clears all entries from the cache
   */
  public void clear()
  {}

  /**
   * @brief Simple LRU Replacement Algorithm
   */
  public void LRU()
  {}

  /**
   * @brief Simple MRU Replacement Algorithm
   */
  public void MRU()
  {}

  /**
   * @brief Indicates a particular function to invoke when cache is full
   * @param String function Name of function to use when cache is full
   */
  public void setReplacementAlgorithm(String function)
  {}

  // @TODO: change to private
  /**
   * @brief Evicts key-value pairs when cache is full and an insertion is required
   */
  private void eviction()
  {}

  /**
   * @brief Retrieves the hashCode based on the key provided
   * @param  K key [description]
   * @return Integer The index of the block in which this key is mapped to
   */
  private int hashCode(K key)
  {

  }

  public class CacheItem<K,V>
  {
    private long timestamp;
    private K key;
    private V value;

    public CacheItem(K key, V value)
    {
      this.key = key;
      this.value = value;
      this.timestamp = getCurrentTime();
    }

    public CacheItem(K key, V value, long timestamp)
    {
      this.key = key;
      this.value = value;
      this.timestamp = timestamp;
    }

    public static long getCurrentTime()
    {
      Date date = new Date(System.currentTimeMillis());
      return date.getTime();
    }

    public V getValue()
    {
      return this.value;
    }
    public K getKey()
    {
      return this.key;
    }
    public long getTimestamp()
    {
      return this.timestamp;
    }

}
