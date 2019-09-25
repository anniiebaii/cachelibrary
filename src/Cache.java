package src;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.lang.reflect.Method;

public class Cache<K,V>
{
  private long timestamp;
  private K key;
  private V value;

  public Cache(K key, V value)
  {
    this.key = key;
    this.value = value;
    this.timestamp = getCurrentTime();
  }

  public Cache(K key, V value, long timestamp)
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
