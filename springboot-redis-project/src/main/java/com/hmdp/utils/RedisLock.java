package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author FENGXIN
 * @date 2024/9/15
 * @project springboot-part
 * @description
 **/
public class RedisLock implements ILock{
    
    public StringRedisTemplate stringRedisTemplate;
    // 业务名
    private String name;
    private static final String LOCK_KEYPREFIX = "lock:";
    
    public RedisLock (StringRedisTemplate stringRedisTemplate , String name) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.name = name;
    }
    
    @Override
    public boolean tryLock (long timeout) {
        // 获取线程id
        long threadId = Thread.currentThread ().threadId ();
        return Boolean.TRUE.equals (stringRedisTemplate.opsForValue ().setIfAbsent (LOCK_KEYPREFIX + name, String.valueOf (threadId) ,timeout, TimeUnit.SECONDS));
    }
    
    @Override
    public void unLock () {
        stringRedisTemplate.delete (LOCK_KEYPREFIX + name);
    }
}
