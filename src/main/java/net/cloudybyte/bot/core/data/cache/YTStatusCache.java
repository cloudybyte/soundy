package net.cloudybyte.bot.core.data.cache;


import java.util.HashMap;

public class YTStatusCache {
    static HashMap<String, String> ytStatusCache = new HashMap<>();

    public static HashMap getytStatusCache(){
        return ytStatusCache;
    }
    public static void initCache(){
        HashMap<String, String> ytStatusCache = new HashMap<String, String>();
    }

    public static String get(String input){
        return ytStatusCache.get(input);
    }
    public static void put(String key, String value){
        ytStatusCache.put(key, value);
    }
    public static void clear(){
        ytStatusCache.clear();
    }
    public static void remove(String key){
        ytStatusCache.remove(key);
    }

}