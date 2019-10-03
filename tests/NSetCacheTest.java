import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

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
    assertEquals(true, true );
  }

  @Test
  public void test_defineAlg()
  {

  }

  // @Test(expected = NoSuchMethodException.class)
  @Test
  public void test_defineInvalidAlg()
  {

  }

  @Test
  public void test_get()
  {}

  @Test
  public void test_set()
  {}

  @Test
  public void test_delete()
  {

  }

  @Test
  public void test_getCollection()
  {

  }

  @Test
  public void test_deleteCollection()
  {

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
  public void test_hashCode()
  {

  }
}
