# N-way Set-Associative Cache


## Description

The purpose of this exercise is to implement a N-Way Set-Associative Cache that is accessible to users through a library.

This cache should support all types in Java for its keys and values, and an instantiation of stated cache must have a declared key and value type, which can be of different types. All keys and values should be the specified type when reading/writing from the cache, otherwise exceptions will be thrown. Support for all keys and values should also be extended to cache replacement algorithms. The library should also include replacement algorithm flexibilities for users. 

### Approach

The purpose of a cache is to reduce the time it takes for users to retrieve viable information without having to access database and outside servers, and within the constraints of a cache being viable until the end of code execution, this translates to using methods within this library obtain a faster access time for users during run time. With this consideration in mind, this design opted to utilize the O(1) retrieval time of a HashMap data structure in Java. 

The process of retrieving data from this cache follows these steps: <br>

1. Generate a hashCode between 1 and N (inclusive) for a given key
2. Within the outer hashMap, access the inner hashMap associated with the generated hashCode => O(1)
3. Retrieve the value stored using the given key from the selected inner hashMap => O(1) 
4. Return the value to the user 

## Technologies

JDK 8+

## Structure

Compile and use the library by using the following steps:

1. Compile associated library files<br>
`javac CacheLibrary.java NSetCache.java`

2. Build the library containing the Cache Library ([src](https://docs.oracle.com/javase/tutorial/deployment/jar/build.html))<br>
`jar -cvf MyJarFile.jar CacheLibrary.class NSetCache.class`

3. Compile main execution file with `.jar` file <br>
`javac -cp MyJarFile.jar main.java`

4. Execute main file <br>
`java main`

## Data Layer

* The Cache will use a nested HashMap data structure that looks like this:
```
(1) => array(key => CacheItem, key => CacheItem),
...
(n) => array(key => CacheItem, key => CacheItem)
```


### CacheLibrary Interface 
```java
public interface CacheLibrary<K,V>
{
  /**
   * @brief Retrieves a value from the cache by a given key
   * @return V Value corresponding to given key
   */
  public V get(K key);

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
  public HashMap<K,V> getCollection(K[] keys);

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
   * @brief Simple LRU Replacement Algorithm
   */
  public void LRU();

  /**
   * @brief Simple MRU Replacement Algorithm
   */
  public void MRU();

  /**
   * @brief Indicates a particular function to invoke when cache is full
   * @param String function Name of function to use when cache is full
   */
  public void setReplacementAlgorithm(String function);

  /**
   * @brief Evicts key-value pairs when cache is full and an insertion is required
   */
  private void eviction();

}
```

### CacheItem Class

Class used internally by NSetCache to store a cache entry as an object

```
private class CacheItem<K,V>
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
```

### NSetCache Base Class implementing the interface

```java
public class NSetCache
{
  // data layer of 2-set Cache using a HashMap
  /*
      {
      "1": {
        "Key": "value",
        "Key": "value"
      },
      "2": {
        "Key": "value",
        "Key": "value"
      }
    }
*/
  
  // private variables
  private final int N;
  private final String name;
  private final HashMap<Integer, HashMap<K,CacheItem>> cache = new HashMap<Integer, HashMap<K,CacheItem>>();
  
  // implements all interface methods
    
  /**
   * @brief Basic Constructor
   * @param String name The name of the cache
   * @param Integer n The number of sets and entries in each set
   */
  public NSetCache(String name, Integer n)
  { ... }
  
  
  /**
   * @brief Constructor
   * @param String name The name of the cache
   * @param Integer n The number of sets and entries in each set
   * @param HashMap<Integer, HashMap<K, CacheItem>> loadedMap Existing cache data to load into current cache
   */
  public NSetCache(String name, Integer n, HashMap<Integer, HashMap<K, CacheItem>> loadedMap)
  { ... }

  /**
   * @brief Retrieves a hashCode based on the key provided
   * @param  K key 
   * @return Integer The index of the block in which this key is mapped to (between 1 and N, inclusive)
   */
  private int hashCode(K key)
  { ... }
}
```

## Scalability
This is a general usage cache with lots of flexibility allowed for users and will only support N blocks with N entries for cache data storage. It will be the user's responsibility to allocate an appropriate amount of N blocks upon cache instantiation for their specific use case.

## Risk Analysis
Because the following library does not utilize any database or data structure connections of any sort and will be distributed in similar form of a `.jar` file, the library source code is protected and no server/database overload will occur on our end. The data contained in this cache will only be viable until the end of code execution.

## Deliverables
- [ ] CacheLibrary Interface
- [ ] NSetCache Class implements CacheLibrary
- [ ] CacheItem Class
- [ ] Unit Tests

## Unit Test Checklist 
- [ ] NSetCache Base Class functions
- [ ] Cache Library compilation/import usage

## Task Breakdown
- [ ] CacheItem Class
- [ ] CacheLibrary Interface
- [ ] NSetCache: CacheLibrary interface implementations
- [ ] NSetCache: Hashing implementations
- [ ] NSetCache: LRU/MRU implementations
- [ ] Unit Tests
