package src;
import java.io.*;
import java.util.*;
import java.lang.*;

public class CacheItem<K,V>
{
  private long timestamp;
  private K key;
  private V value;

  public CacheItem(K key, V value)
  {
    this.key = key;
    this.value = value;
    this.timestamp = this.getCurrentTime();
  }

  public CacheItem(K key, V value, long timestamp)
  {
    this.key = key;
    this.value = value;
    this.timestamp = timestamp;
  }

  public long getCurrentTime()
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
