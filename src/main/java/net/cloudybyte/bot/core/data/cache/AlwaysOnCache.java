package net.cloudybyte.bot.core.data.cache;


import java.util.HashMap;

public class AlwaysOnCache{
      static HashMap<String, String> alwaysOnCache = new HashMap<>();

      public static HashMap getAlwaysOnCache(){
         return alwaysOnCache;
      }
      public static void initCache(){
            HashMap<String, String> alwaysOnCache = new HashMap<String, String>();
      }

    public static String get(String input){
          String returnable = alwaysOnCache.get(input);
          if (returnable == null || returnable.equals("null")){
              returnable = "off";
          }
        return returnable;
    }
    public static void put(String key, String value){
        alwaysOnCache.put(key, value);
    }
    public static void clear(){
          alwaysOnCache.clear();
    }
    public static void remove(String key){
          alwaysOnCache.remove(key);
    }

}