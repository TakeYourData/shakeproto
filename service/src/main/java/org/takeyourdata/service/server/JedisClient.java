package org.takeyourdata.service.server;

import redis.clients.jedis.JedisPooled;

public class JedisClient {
    private final JedisPooled jedis;

    public JedisClient() {
        this.jedis = new JedisPooled("localhost", 6379);
    }

    public JedisPooled getJedis() {
        return jedis;
    }
}
