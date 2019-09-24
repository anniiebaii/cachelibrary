# N-way Set-Associative Cache


## Description

The purpose of this exercise is to implement a N-Way Set-Associative Cache that is accessible to users through a library.

This cache should support all types in Java for its keys and values, and an instantiation of stated cache must have a declared key and value type, which can be of different types. All keys and values should be the specified type when reading/writing from the cache, otherwise exceptions will be thrown. Support for all keys and values should also be extended to cache replacement algorithms. The library should also include replacement algorithm flexibilities for users. 

## Technologies

JDK 8+

## Structure

Compile and use the library by using the following steps:
1. Compile the java files

`javac cachelibrary.java`

2. Build the library

`build lib cachelibrary`

3. Import the library into your java file to be used

`import cachelibrary`

## Data Layer

* The Cache will use a HashMap data structure that looks like this:
```
(0) => array(key => value, key => value),
...
(n) => array(key => value, key => value)
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

  // @TODO: change to private
  /**
   * @brief Evicts key-value pairs when cache is full and an insertion is required
   */
  public void eviction();

}
```

### Cache Base Class

```java
public class Cache
{
  // implements all interface methods
  
  /**
   * @brief Basic constructor
   * @param String name
   * @param Integer n
   * @param HashMap<K,V> loadedMap
   */  
  public Cache(String name, int n, HashMap<K,V> loadedMap)
  { ... }

  // hashing function
  private K hash(K key)
  { ... }
  
  // encryption/decryption functions
  private K encryptKey(K key)
  { ... }
  
  private K decryptKey(K key)
  { ... }
}
```

## Scalability
This is a general usage cache with lots of flexibility allowed for users and will only support N blocks for cache storage. It will be the user's responsibility to allocate an appropriate amount of N blocks for their specific use case.

## Security/Potential Abuse
Keys used to access values in cache will be encrypted/decrypted within the cache library for security measures.

## Risk Analysis
Because the following library does not utilize any database or data structure connections of any sort and will be distributed in the form of a `.lib` file, the library source code is protected and no server overload will occur. This data contained in this cache will only be viable until the end of code execution.

## Deliverables
- [ ] CacheLibrary Interface
- [ ] Cache Class
- [ ] Unit Tests

## Unit Test Checklist 
- [ ] Cache Base Class functions
- [ ] Cache Library compilation
- [ ] Cache Library import usage

## Task Breakdown
- [ ] CacheLibrary Interface
- [ ] Cache class interface implementations
- [ ] Cache class Hashing implementations
- [ ] Cache class LRU/MRU implementations
- [ ] Unit Tests
