package com.fengxin.springboot.redis;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author FENGXIN
 * @date 2024/9/5
 * @project springboot-part
 * @description
 **/
@SpringBootTest
public class RedisSpringDataTests {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    
    @Test
    void testString(){
        redisTemplate.opsForValue ().set("name","枫叶");
        System.out.println (redisTemplate.opsForValue ().get ("name"));
    }
}
