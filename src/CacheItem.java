package src;
import java.io.*;
import java.util.*;
import java.lang.*;

public class CacheItem
{
  private long timestamp;
  private Object key;
  private Object value;
  public boolean isEmpty;

  public CacheItem(Object key, Object value)
  {
    if (value == null)
    {
      this.isEmpty = true;
    }
    else
    {
      this.isEmpty = false;
    }
    this.key = key;
    this.value = value;
    this.timestamp = this.getCurrentTime();
  }

  public CacheItem(Object key, Object value, long timestamp)
  {
    if (value == null)
    {
      this.isEmpty = true;
    }
    else
    {
      this.isEmpty = false;
    }
    this.key = key;
    this.value = value;
    this.timestamp = timestamp;
  }

  public long getCurrentTime()
  {
    Date date = new Date(System.currentTimeMillis());
    return date.getTime();
  }

  public Object getValue()
  {
    return this.value;
  }
  public Object getKey()
  {
    return this.key;
  }
  public long getTimestamp()
  {
    return this.timestamp;
  }
  public void updateTimestamp()
  {
    this.timestamp = this.getCurrentTime();
  }

  public boolean empty()
  {
    System.out.println("is empty check");
    return this.isEmpty;
  }

}
