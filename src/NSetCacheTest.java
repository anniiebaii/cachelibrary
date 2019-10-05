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
import src.NSetCache;
import src.CacheItem;

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
      name.setAccessible(true);
      Field N_sets = cache.getClass().getDeclaredField("N_sets");
      N_sets.setAccessible(true);
      Field M_blocks = cache.getClass().getDeclaredField("M_blocks");
      M_blocks.setAccessible(true);
      Field map = cache.getClass().getDeclaredField("cache");
      map.setAccessible(true);
      Field method = cache.getClass().getDeclaredField("method");
      method.setAccessible(true);

      assertEquals("cache", name.get(cache));
      assertEquals(5, N_sets.get(cache));
      assertEquals(2, M_blocks.get(cache));
      HashMap<Integer, HashMap<Integer, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<Integer, CacheItem>>)map.get(cache);
      assertEquals(5, cachedBlocks.size());
      assertEquals("LRU", method.get(cache));
    }
    catch (NoSuchFieldException | IllegalAccessException e)
    {
      assertTrue(false);
    }
  }

  // @Test(expected = NoSuchMethodException.class)
  @Test
  public void test_defineInvalidAlg()
  {

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
    cache.set(1, "Hello World");
    try
    {
      Field map = cache.getClass().getDeclaredField("cache");
      map.setAccessible(true);
      HashMap<Integer, HashMap<Integer, CacheItem>> cachedBlocks = (HashMap<Integer, HashMap<Integer, CacheItem>>)map.get(cache);
      assertEquals(5, cachedBlocks.size());
      int index = cache.getHash(1);
      assertEquals("Hello World", cachedBlocks.get(index).get(1).getValue());

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

    CacheItem nullEntry = cache.get(1);
    assertNull(nullEntry);
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

    // verify actually set in cache
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

    System.out.println("new obj");
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

    // @TODO reflect
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
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 1, 2, "LRU");
    cache.set(1, "First");
    cache.set(2, "Second");
    cache.set(3, "Third");

    // check that First is deleted
    assertNull(cache.get(1));
    // check that Second is not deleted
    assertNotNull(cache.get(2));
    assertEquals("Second", cache.get(2).getValue());
    // check that Third is inserted
    assertNotNull(cache.get(3));
    assertEquals("Third", cache.get(3).getValue());
  }

  @Test
  public void test_MRU()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 1, 2, "MRU");
    cache.set(1, "First");
    cache.set(2, "Second");
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
  public void test_customReplacementAlgorithm()
  {

  }

  @Test
  public void test_eviction()
  {

  }

  @Test
  public void test_getHash()
  {
    NSetCache<Object,Integer> cache = new NSetCache<Object,Integer>("cache", 5, 1);
    // testing hash for different types
    SampleObject obj1 = new SampleObject("1");
    String obj2 = "Hello World";
    Integer obj3 = 999;
    boolean obj4 = true;
    double obj5 = 4.4;
    SampleObject obj6 = new SampleObject("1");
    String[] obj7 = {"1", "2", "3"};

    assertTrue(cache.getHash(obj1) < 5 && cache.getHash(obj1) >= 0);
    assertTrue(cache.getHash(obj1) == cache.getHash(obj6));
    assertTrue(cache.getHash(obj2) < 5 && cache.getHash(obj2) >= 0);
    assertTrue(cache.getHash(obj3) < 5 && cache.getHash(obj3) >= 0);
    assertTrue(cache.getHash(obj4) < 5 && cache.getHash(obj4) >= 0);
    assertTrue(cache.getHash(obj5) < 5 && cache.getHash(obj5) >= 0);
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
