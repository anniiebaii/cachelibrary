package src;
import java.io.*;
import java.util.*;
import java.lang.*;

public interface CacheLibrary<K,V>
{
  // @TODO: handle dynamic types of keys/values after getting info for resume
  /**
   * @brief Retrieves a value from the cache by a given key
   * @return [description]
   */
  public V get(K key);

  /**
   * @brief Inserts a key-value pair into the cache
   * @param Object key   [description]
   * @param Object value [description]
   */
  public boolean set(K key, V value);

  /**
   * @brief Deletes a key-value entry from the cache by a given key
   * @param Object key [description]
   */
  public boolean delete(K key);

 /**
  * @brief Retrieves key-value pairs from the cache by the given keys
  * @return HashMap<K,V> collection
  */
  public HashMap<K,V> getCollection(K[] keys);

 /**
  * @brief Inserts key-value pairs into the cache
  * @param HashMap<Object,Object> entries
  */
  public boolean setCollection(HashMap<K, V> entries);

  /**
   * @brief Deletes key-value entries from the cache that corresponds to given keys
   * @param Object key [description]
   */
  public boolean deleteCollection(K keys[]);

  /**
   * @brief Clears all entries from the cache
   */
  public void clear();

  public void LRU();

  public void MRU();

}
