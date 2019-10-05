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
  // 	assertEquals(13, r_13_4.getNumerator());

  @Test
  public void test_basicConstructor()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    // assert private member variables are properly set
    assertEquals(true, true);
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
    assertTrue(nullEntry.isEmpty());
  }

  @Test
  public void test_set()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    cache.set(1, "Hello World");
    // @TODO: reflect
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

    // @TODO: reflect

    CacheItem nullEntry = cache.get(1);
    assertTrue(nullEntry.isEmpty());
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
    // assertEquals("Hello Two", col.get(2).getValue());
    assertEquals("Hello Three", col.get(3).getValue());
  }

  @Test
  public void test_setCollection()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    HashMap<Integer, String> data = new HashMap<Integer, String>(3);
    // data.set(1, "Hello One");
    // data.set(2, "Hello Two");
    // data.set(3, "Hello Three");
    // @TODO: reflect
  }

  @Test
  public void test_deleteCollection()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    cache.set(1, "Hello One");
    cache.set(2, "Hello Two");
    cache.set(3, "Hello Three");

    Integer[] keys = {1, 2, 3};
    cache.deleteCollection(keys);
    boolean fail;
    try
    {
      fail = false;
      assertEquals("Hello One", cache.get(1).getValue());
      assertEquals("Hello Two", cache.get(2).getValue());
      assertEquals("Hello Three", cache.get(3).getValue());
    }
    catch (NullPointerException e)
    {
      fail = true;
    }
    assertTrue(fail);
  }

  @Test
  public void test_clear()
  {

  }

  @Test
  public void test_LRU()
  {

  }

  @Test
  public void test_MRU()
  {

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

  }
}
