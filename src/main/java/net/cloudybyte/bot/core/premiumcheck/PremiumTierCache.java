package net.cloudybyte.bot.core.premiumcheck;

import java.util.HashMap;

public class PremiumTierCache {
    public static HashMap<String, Enum> customerCache;

    public static void setCustomerCache(HashMap<String, Enum> customerCache) {
        PremiumTierCache.customerCache = customerCache;
    }
    public static HashMap<String, Enum> getCustomerCache() {
        if(customerCache == null){
            HashMap<String, Enum> customerCache = new HashMap<>();
        }
        return customerCache;
    }
}
