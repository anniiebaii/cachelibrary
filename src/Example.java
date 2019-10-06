import java.io.*;
import java.util.*;
import java.lang.*;

public class Example
{
  public static void main(String[] args)
  {
    System.out.println("Using Example");
    NSetCache<Integer,Integer> cache = new NSetCache<Integer,Integer>("cache", 2, 1);
    cache.set(1, 2);

  }
}
