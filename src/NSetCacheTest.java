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
    assertNull(nullEntry);
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
    // assertEquals("Hello Two", col.get(2).getValue());
    assertEquals("Hello Three", col.get(3).getValue());
  }

  @Test
  public void test_setCollection()
  {
    NSetCache<Integer, String> cache = new NSetCache<Integer, String>("cache", 5, 2);
    HashMap<Integer, String> data = new HashMap<Integer, String>(3);
    data.put(1, "Hello One");
    data.put(2, "Hello Two");
    data.put(3, "Hello Three");
    cache.setCollection(data);
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
    assertTrue(cache.deleteCollection(keys));
    assertNull(cache.get(1));
    assertNull(cache.get(2));
    assertNull(cache.get(3));
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

  }
}
