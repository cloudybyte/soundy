package net.cloudybyte.bot.core.data.cache;


import java.util.HashMap;

public class YTKeyCache {
      static HashMap<String, String> ytKeyCache = new HashMap<>();

      public static HashMap getytKeyCache(){
         return ytKeyCache;
      }
      public static void initCache(){
            HashMap<String, String> ytKeyCache = new HashMap<String, String>();
      }

    public static String get(String input){
        return ytKeyCache.get(input);
    }
    public static void put(String key, String value){
        ytKeyCache.put(key, value);
    }
    public static void clear(){
          ytKeyCache.clear();
    }
    public static void remove(String key){
          ytKeyCache.remove(key);
    }

}