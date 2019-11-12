import java.io.*;
import java.util.*;
import java.lang.*;
import java.time.Instant;

public class CacheItem
{
  private Instant timestamp;
  private Object key;
  private Object value;
  public boolean isEmpty;

  public CacheItem(Object key, Object value)
  {
    this.key = key;
    this.value = value;
    this.timestamp = Instant.now();
    // System.out.println("TIME: " + this.timestamp);
  }

  public CacheItem(Object key, Object value, Instant timestamp)
  {
    this.key = key;
    this.value = value;
    this.timestamp = timestamp;
  }

  public Object getValue()
  {
    return this.value;
  }
  public Object getKey()
  {
    return this.key;
  }
  public Instant getTimestamp()
  {
    return this.timestamp;
  }
  public void updateTimestamp()
  {
    //this.timestamp = this.getCurrentTime();
    this.timestamp = Instant.now();
  }
}
