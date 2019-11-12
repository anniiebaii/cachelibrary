import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.security.*;
import java.lang.reflect.*;
import java.util.concurrent.TimeUnit;

public class NSetCacheTest
{
  @Rule
  public TestName name = new TestName();

  @Before
  public void setUp()
  {
    System.out.println("Starting " + this.name.getMethodName() + " ...");
  }

  @Test
  public void test_basicConstructor()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);

    // assert private member variables are properly set
    try
    {
      Field name = cache.getClass().getDeclaredField("name");
      Field N_sets = cache.getClass().getDeclaredField("N_sets");
      Field M_blocks = cache.getClass().getDeclaredField("M_blocks");
      Field map = cache.getClass().getDeclaredField("cache");
      Field method = cache.getClass().getDeclaredField("method");

      name.setAccessible(true);
      N_sets.setAccessible(true);
      M_blocks.setAccessible(true);
      map.setAccessible(true);
      method.setAccessible(true);

      assertEquals("cache", name.get(cache));
      assertEquals(5, N_sets.get(cache));
      assertEquals(2, M_blocks.get(cache));
      HashMap<Integer, HashMap<Integer, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<Integer, CacheItem>>)map.get(cache);
      assertEquals(5, cachedBlocks.size());
      assertEquals(NSetCache.LEAST_RECENT, method.get(cache));
    }
    catch (NoSuchFieldException | IllegalAccessException e)
    {
      assertTrue(false);
    }
  }

  @Test (expected = IllegalArgumentException.class)
  public void test_defineInvalidAlg()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2, "trash");
  }

  @Test
  public void test_get()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    cache.set(1, "Hello World");
    CacheItem entry = cache.get(1);
    assertEquals("Hello World", entry.getValue());
    CacheItem nullEntry = cache.get(2);
    assertNull(nullEntry);
  }

  @Test
  public void test_set()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    assertTrue(cache.set(1, "Hello World"));
    assertFalse(cache.set(1, "Hello World"));

    // check that values are actually set
    try
    {
      Field map = cache.getClass().getDeclaredField("cache");
      map.setAccessible(true);
      HashMap<Integer, HashMap<Integer, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<Integer, CacheItem>>)map.get(cache);
      assertEquals(5, cachedBlocks.size());
      int index = cache.getHash(1);
      assertEquals("Hello World", cachedBlocks.get(index).get(1).getValue());

      cachedBlocks = (HashMap<Integer, HashMap<Integer, CacheItem>>)map.get(cache);
      assertFalse(cache.set(1, "Replace me!"));
      assertEquals("Replace me!", cachedBlocks.get(index).get(1).getValue());
    }
    catch (NoSuchFieldException | IllegalAccessException e)
    {
      assertTrue(false);
    }
  }

  @Test
  public void test_delete()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    cache.set(1, "Hello World");
    CacheItem entry = cache.get(1);
    assertEquals("Hello World", entry.getValue());
    boolean result = cache.delete(1);
    assertTrue(result);

    // check that it is not retrievable
    CacheItem nullEntry = cache.get(1);
    assertNull(nullEntry);

    // check that it is actually deleted
    try
    {
      Field map = cache.getClass().getDeclaredField("cache");
      map.setAccessible(true);
      HashMap<Integer, HashMap<Integer, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<Integer, CacheItem>>)map.get(cache);
      assertEquals(5, cachedBlocks.size());
      int index = cache.getHash(1);
      assertNull(cachedBlocks.get(index).get(1));
    }
    catch (NoSuchFieldException | IllegalAccessException e)
    {
      assertTrue(false);
    }
  }

  @Test
  public void test_getCollection()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    cache.set(1, "Hello One");
    cache.set(2, "Hello Two");
    cache.set(3, "Hello Three");

    Integer[] keys = {1, 2, 3};
    HashMap<Integer, CacheItem> col = cache.getCollection(keys);
    assertEquals("Hello One", col.get(1).getValue());
    assertEquals("Hello Two", col.get(2).getValue());
    assertEquals("Hello Three", col.get(3).getValue());
  }

  @Test
  public void test_setCollection()
  {
    NSetCache<String, String> cache = new NSetCache<String, String>("cache", 5, 2);
    HashMap<String, String> data = new HashMap<String, String>(3);
    data.put("1", "Hello One");
    data.put("2", "Hello Two");
    data.put("3", "Hello Three");
    cache.setCollection(data);

    // check that it is actually set
    try
    {
      Field map = cache.getClass().getDeclaredField("cache");
      map.setAccessible(true);
      HashMap<Integer, HashMap<String, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<String, CacheItem>>)map.get(cache);
      assertEquals(5, cachedBlocks.size());
      int one_index = cache.getHash("1");
      int two_index = cache.getHash("2");
      int three_index = cache.getHash("3");

      assertEquals("Hello One", cachedBlocks.get(one_index).get("1").getValue());
      assertEquals("Hello Two", cachedBlocks.get(two_index).get("2").getValue());
      assertEquals("Hello Three", cachedBlocks.get(three_index).get("3").getValue());
    }
    catch (NoSuchFieldException | IllegalAccessException e)
    {
      assertTrue(false);
    }

    // testing with sample object class
    NSetCache<SampleObject, String> ObjCache = new NSetCache<SampleObject, String>("ObjCache", 5, 2);
    SampleObject one = new SampleObject("Hello One");
    SampleObject two = new SampleObject("Hello Two");
    SampleObject three = new SampleObject("Hello Three");

    ObjCache.set(one, "Hello One");
    ObjCache.set(two, "Hello Two");
    ObjCache.set(three, "Hello Three");

    try
    {
      Field map = ObjCache.getClass().getDeclaredField("cache");
      map.setAccessible(true);
      HashMap<Integer, HashMap<SampleObject, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<SampleObject, CacheItem>>)map.get(ObjCache);
      assertEquals(5, cachedBlocks.size());

      SampleObject oneDup = new SampleObject("Hello One");

      int one_index = ObjCache.getHash(oneDup);

      assertEquals("Hello One", cachedBlocks.get(one_index).get(oneDup).getValue());
    }
    catch (NoSuchFieldException | IllegalAccessException e)
    {
      assertTrue(false);
    }
  }

  @Test
  public void test_deleteCollection()
  {
    NSetCache<SampleObject, String> cache = new NSetCache<SampleObject, String>("cache", 5, 2);
    SampleObject one = new SampleObject("Hello One");
    SampleObject two = new SampleObject("Hello Two");
    SampleObject three = new SampleObject("Hello Three");

    cache.set(one, "Hello One");
    cache.set(two, "Hello Two");
    cache.set(three, "Hello Three");

    SampleObject[] keys = {one, two, three};
    assertTrue(cache.deleteCollection(keys));
    assertNull(cache.get(one));
    assertNull(cache.get(two));
    assertNull(cache.get(three));
  }

  @Test
  public void test_clear()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    HashMap<Integer, String> data = new HashMap<Integer, String>(3);
    data.put(1, "Hello One");
    data.put(2, "Hello Two");
    data.put(3, "Hello Three");
    cache.setCollection(data);
    cache.clear();

    // test that cache blocks are clear, but empty N sets remain
    try
    {
      Field map = cache.getClass().getDeclaredField("cache");
      map.setAccessible(true);

      HashMap<Integer, HashMap<Integer, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<Integer, CacheItem>>)map.get(cache);
      assertEquals(5, cachedBlocks.size());

      int one_index = cache.getHash(1);
      int two_index = cache.getHash(2);
      int three_index = cache.getHash(3);

      assertNull(cachedBlocks.get(one_index).get(1));
      assertNull(cachedBlocks.get(two_index).get(2));
      assertNull(cachedBlocks.get(three_index).get(3));
    }
    catch (NoSuchFieldException | IllegalAccessException e)
    {
      assertTrue(false);
    }
  }

  @Test
  public void test_LRU()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 1, 2, NSetCache.LEAST_RECENT);
    cache.set(1, "First");
    cache.set(2, "Second");
    cache.get(1);
    cache.set(3, "Third");
    // check that Second is deleted
    assertNull(cache.get(2));
    // check that First is not deleted
    assertNotNull(cache.get(1));
    assertEquals("First", cache.get(1).getValue());
    // check that Third is inserted
    assertNotNull(cache.get(3));
    assertEquals("Third", cache.get(3).getValue());
  }

  @Test
  public void test_MRU()
  {
    NSetCache<Integer, Integer> cache = new NSetCache<Integer, Integer>("cache", 1, 2, NSetCache.MOST_RECENT);
    for (int i = 1; i < 4; i++)
    {
      cache.set(i, i);
    }

    // check that Second is deleted
    assertNull(cache.get(2));
    // check that First is not deleted
    assertNotNull(cache.get(1));
    assertEquals(1, cache.get(1).getValue());
    // check that Third is inserted
    assertNotNull(cache.get(3));
    assertEquals(3, cache.get(3).getValue());
  }

  @Test
  public void test_getHash()
  {
    int N_sets = 5;
    NSetCache<Object,Integer> cache = new NSetCache<Object,Integer>("cache", N_sets, 1);
    // testing hash for different types
    SampleObject obj1 = new SampleObject("1");
    String obj2 = "Hello World";
    Integer obj3 = 999;
    boolean obj4 = true;
    double obj5 = 4.4;
    SampleObject obj6 = new SampleObject("1");
    String[] obj7 = {"1", "2", "3"};

    assertTrue(cache.getHash(obj1) < N_sets && cache.getHash(obj1) >= 0);
    assertTrue(cache.getHash(obj2) < N_sets && cache.getHash(obj2) >= 0);
    assertTrue(cache.getHash(obj3) < N_sets && cache.getHash(obj3) >= 0);
    assertTrue(cache.getHash(obj4) < N_sets && cache.getHash(obj4) >= 0);
    assertTrue(cache.getHash(obj5) < N_sets && cache.getHash(obj5) >= 0);
    // check that hash for different object references, but equal by the equals() function
    // returns equal hash values for this cache
    assertTrue(cache.getHash(obj1) == cache.getHash(obj6));
  }

  @Test
  public void test_getCount()
  {
    NSetCache<Integer, Integer> cache = new NSetCache<Integer, Integer>("cache", 1, 10);
    for (int i = 0; i < 4; i++)
    {
      cache.set(i, i);
    }
    assertEquals(4, cache.getCount());
  }

  @Test
  public void test_workflow()
  {
    int N_entries = 5;
    NSetCache<Integer,Integer> cache = new NSetCache<Integer,Integer>("cache", 1, N_entries);
    for (int i = 0; i < 50; i++)
    {
      cache.set(i, i);
    }
    // check that least recent entries are evicted
    for (int i = 45; i < 50; i++)
    {
      assertEquals(i, cache.get(i).getValue());
    }
  }

  @Test
  public void test_EqualObjects()
  {
    int N_entries = 5;
    NSetCache<SampleObject,Integer> obj_cache = new NSetCache<SampleObject,Integer>("cache", 1, N_entries);
    for (int i = 0; i < 50; i++)
    {
      SampleObject obj = new SampleObject("sample");
      obj_cache.set(obj, i);
    }
    // check that only one entry exists
    assertEquals(49, obj_cache.get(new SampleObject("sample")).getValue());
    assertEquals(1, obj_cache.getCount());
  }

/**
 * Sample Object class used with this Cache Library to illustrate support with various objects
 * @Note that hashCode and equals methods are overridden to define object equality
 */
  private class SampleObject
  {
    private String name;
    public SampleObject(String name)
    {
      this.name = name;
    }
    public String getName()
    {
      return this.name;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj.getClass()!= this.getClass())
      {
        return false;
      }
      if (obj == this)
      {
        return true;
      }
      SampleObject sobj = (SampleObject) obj;
      if (this.name == sobj.getName())
      {
        return true;
      }
      return false;
    }

    @Override
    public int hashCode()
    {
      return Objects.hash(this.name);
    }

  }
}
