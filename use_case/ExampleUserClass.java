import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * Example User Class run with command "ant run"
 */
public class ExampleUserClass
{
  public static void main(String[] args)
  {
    System.out.println("Using Example");
    NSetCache<Integer,Integer> cache = new NSetCache<Integer,Integer>("cache", 2, 1);

    cache.set(1, 2);

  }
}
