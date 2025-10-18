package org.takeyourdata.service.server.databases;

import org.takeyourdata.service.server.ConfigProperties;
import redis.clients.jedis.JedisPooled;

import java.util.Properties;

public class JedisClient {
    private final JedisPooled jedis;

    public JedisClient() {
        Properties config = new ConfigProperties().get();
        String host = config.getProperty("database.redis.host");
        int port = (int) config.get("database.redis.port");
        String username = config.getProperty("database.redis.username");
        String password = config.getProperty("database.redis.password");
        this.jedis = new JedisPooled(host, port, username, password);
    }

    public JedisPooled getJedis() {
        return jedis;
    }
}
