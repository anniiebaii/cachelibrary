package src;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.Method;

public class Cache<K,V> implements CacheLibrary<K,V>
{
  /**
   * @brief Cache storage for key-value pairs
   */
  private final HashMap<K,V> cache = new HashMap<K,V>();
  private final String name;
  private Method replacementAlgorithm;

  /**
   * @brief Basic constructor
   * @param String name
   * @param HashMap<K,V> loadedMap
   */
  public Cache(String name, HashMap<K,V> loadedMap)
  {
    if (loadedMap != null)
    {
      this.cache.putAll(loadedMap);
    }

    this.name = name;
  }

  /**
   * @brief Retrieves a value from the cache by a given key
   * @return [description]
   */
  public V get(K key)
  {
    K decryptedKey = this.decryptKey(key);

    return this.cache.get(decryptedKey);
  }

  /**
   * @brief Inserts a key-value pair into the cache
   * @param K key   [description]
   * @param V value [description]
   */
  public boolean set(K key, V value)
  {
    if (this.cache.put(key,value) != null)
    {
      return true;
    }
    return false;
  }

  /**
   * @brief Deletes a key-value entry from the cache by a given key
   * @param K key [description]
   */
  public boolean delete(K key)
  {
    if (this.cache.remove(key) != null)
    {
      return true;
    }
    return false;
  }

  /**
   * @brief Retrieve values from cache based on collection of keys
   * @param K[] keys Collection of keys to retrieve values from the cache
   * @return HashMap<K,V> collection of key-value pairs
   */
  public HashMap<K,V> getCollection(K[] keys)
  {
    HashMap<K,V> collection = new HashMap<K,V>();
    for (K key : keys)
    {
      K decryptedKey = this.decryptKey(key);
      K encryptedKey = this.encryptKey(key);
      collection.put(encryptedKey, this.cache.get(decryptedKey));
    }
    return collection;
  }

  /**
   * @brief Deletes key-value entries from the cache that corresponds to given keys
   * @param Object key [description]
   */
  public boolean deleteCollection(K[] keys)
  {
    for (K key : keys)
    {
      K encryptedKey = this.encryptKey(key);
      this.cache.remove(encryptedKey);
    }
    return true;
  }

 /**
  * @brief Inserts key-value pairs into the cache
  * @param HashMap<K,V> entries
  */
  public boolean setCollection(HashMap<K, V> entries)
  {
    for (HashMap.Entry<K,V> entry : entries.entrySet())
    {
      K key = entry.getKey();
      V value = entry.getValue();
      this.cache.put(encryptKey(key), value);
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

  public void show()
  {
    System.out.println("Hello World!");
  }

  public void LRU()
  {}

  public void MRU()
  {}

  public void setReplacementAlgorithm(String function)
  {
    try
    {
      Method method = Cache.class.getDeclaredMethod(function);
      this.replacementAlgorithm = method;
    }
    catch (NoSuchMethodException e)
    {
      System.out.println("Invalid method used as new replacement algorithm.");
    }
  }

  public void test()
  {
    System.out.println("WORKED REFLECT");
  }

  // @TODO: change to private
  public void eviction()
  {
    try
    {
      this.setReplacementAlgorithm("tesst"); // test, can be removed
      this.replacementAlgorithm.invoke(this);
    }
    catch (Exception e)
    {
      System.out.println("Replacement algorithm must declared be in cache class.");
    }
  }

  private K encryptKey(K key)
  {
    return key;
  }

  private K decryptKey(K key)
  {
    return key;
  }
}
