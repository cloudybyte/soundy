package net.cloudybyte.bot.core.data;

import net.cloudybyte.bot.core.audio.PlayerManager;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisInstanceHolder {
    static JedisPool pool;
    public static synchronized JedisPool getInstance() {
        if (pool == null) {
            pool = new JedisPool(new JedisPoolConfig(), "raspberrypi", 6380);
        }
        return pool;
    }
}
