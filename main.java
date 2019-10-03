package src;
import java.io.*;
import java.util.*;
import java.lang.*;

class main
{
  public static void main(String[] args)
  {
    NSetCache<Integer,Integer> cache = new NSetCache<Integer,Integer>("cache", 5, 1);
    // cache.show();
    // /*
    //   error: incompatible types: String cannot be converted to Integer
    //   cache.get("1");
    //  */
    // cache.get(1);
    // // cache.eviction();
    // HashMap<Integer,Integer> test = new HashMap<Integer,Integer>(5);
    // System.out.println(test.size());
  }
}
