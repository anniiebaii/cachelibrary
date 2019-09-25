package src;

class main
{
  public static void main(String[] args)
  {
    NSetCache<Integer,Integer> cache = new NSetCache<Integer,Integer>("cache", 5, null);
    cache.show();
    /*
      error: incompatible types: String cannot be converted to Integer
      cache.get("1");
     */
    cache.get(1);
    cache.eviction();
  }
}
