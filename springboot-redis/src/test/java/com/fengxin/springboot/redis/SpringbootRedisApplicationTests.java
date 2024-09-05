package com.fengxin.springboot.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class SpringbootRedisApplicationTests {
    private Jedis jedis;
    
    /**
     * 连接redis
     */
    @BeforeEach
    void before () {
        // 连接本地的 Redis 服务
        jedis = new Jedis("192.168.187.101",6379);
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
    
    @AfterEach
    void after(){
        if(jedis != null){
            jedis.close ();
        }
    }
    
}
