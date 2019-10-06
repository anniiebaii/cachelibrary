import java.io.*;
import java.util.*;
import java.lang.*;

public interface CacheLibrary<K,V>
{
  /**
   * @brief Retrieves a value from the cache by a given key
   * @return V Value corresponding to given key
   */
  public CacheItem get(K key);

  /**
   * @brief Inserts a key-value pair into the cache
   * @param K key
   * @param V value
   * @return boolean True on success
   */
  public boolean set(K key, V value);

  /**
   * @brief Deletes a key-value entry from the cache by a given key
   * @param K key
   * @return boolean True on success
   */
  public boolean delete(K key);

 /**
  * @brief Retrieves key-value pairs from the cache by the given keys
  * @return HashMap<K,V> collection
  */
  public HashMap<K,CacheItem> getCollection(K[] keys);

 /**
  * @brief Inserts key-value pairs into the cache
  * @param HashMap<K,V> entries
  */
  public boolean setCollection(HashMap<K, V> entries);

  /**
   * @brief Deletes key-value entries from the cache that corresponds to given keys
   * @param K key
   * @return boolean True on success
   */
  public boolean deleteCollection(K keys[]);

  /**
   * @brief Clears all entries from the cache
   */
  public void clear();

  /**
   * @brief Evicts key-value pairs when cache is full and an insertion is required
   */
  public void eviction(int index);

}
