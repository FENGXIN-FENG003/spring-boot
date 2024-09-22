package com.fengxin.springboot.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author FENGXIN
 * @date 2024/9/5
 * @project springboot-part
 * @description
 **/
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 创建RedisTemplate
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<> ();
        // 设置连接池
        redisTemplate.setConnectionFactory (redisConnectionFactory);
        // key 用string value用json
        // 创建json序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer ();
        // 设置key序列化
        redisTemplate.setKeySerializer (RedisSerializer.string ());
        redisTemplate.setHashKeySerializer (RedisSerializer.string ());
        // 设置value序列化
        redisTemplate.setValueSerializer (jsonRedisSerializer);
        redisTemplate.setHashValueSerializer (jsonRedisSerializer);
        return redisTemplate;
    }
}
