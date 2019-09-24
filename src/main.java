package src;

class main
{
  public static void main(String[] args)
  {
    Cache<Integer,Integer> cache = new Cache<Integer,Integer>("cache", null);
    cache.show();
    /*
      error: incompatible types: String cannot be converted to Integer
      cache.get("1");
     */
    cache.get(1);
    cache.eviction();
  }
}
