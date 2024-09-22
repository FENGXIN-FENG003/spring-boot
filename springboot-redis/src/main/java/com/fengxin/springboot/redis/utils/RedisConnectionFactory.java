package com.fengxin.springboot.redis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @author FENGXIN
 * @date 2024/9/5
 * @project springboot-part
 * @description
 **/
public class RedisConnectionFactory {
    private static final JedisPool JEDIS_POOL;
    static {
        // 配置连接
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig ();
        jedisPoolConfig.setMaxTotal (8);
        jedisPoolConfig.setMaxIdle (8);
        jedisPoolConfig.setMinIdle (1);
        Duration duration = Duration.ofMillis (1000);
        jedisPoolConfig.setMaxWait (duration);
        // 配置redis
        JEDIS_POOL = new JedisPool (jedisPoolConfig,"192.168.187.101",6379,1000,"51213002");
    }
    
    /**
     * 返回redis
     * @return redis
     */
    public static Jedis getJedis(){
        return JEDIS_POOL.getResource ();
    }
}
