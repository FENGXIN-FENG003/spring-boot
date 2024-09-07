package com.hmdp.springboot.redis;

import com.hmdp.springboot.redis.utils.RedisConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.util.List;

@SpringBootTest
class RedisJedisTests {
    private Jedis jedis;
    
    /**
     * 连接redis
     */
    @BeforeEach
    void before () {
        // 连接本地的 Redis 服务
        jedis = RedisConnectionFactory.getJedis ();
        // 设置密码
        jedis.auth("51213002");
        // 选择库
        jedis.select(0);
    }
    @Test
    void testString () {
        jedis.set ("MyName","枫");
        String myName = jedis.get ("MyName");
        System.out.println ("myName = " + myName);
    }
    
    @Test
    void testZSet(){
        jedis.zadd ("users",89.02,"Jack");
        jedis.zadd ("users",99.56,"Lucy");
        List<String> user = jedis.zrangeByScore ("users" , 80 , 100);
        System.out.println ("user = " + user);
    }
    @AfterEach
    void after(){
        if(jedis != null){
            jedis.close ();
        }
    }
    
}
