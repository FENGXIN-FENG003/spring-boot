package com.fengxin.utils;

import cn.hutool.core.lang.UUID;
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
    // 唯一线程id 解决误删🔒问题
    private static final String ID_KEYPREFIX = UUID.fastUUID () + "-";
    
    public RedisLock (StringRedisTemplate stringRedisTemplate , String name) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.name = name;
    }
    
    @Override
    public boolean tryLock (long timeout) {
        // 获取线程id
        String threadId = ID_KEYPREFIX + Thread.currentThread ().threadId ();
        // 设置超时 防止redis宕机不释放锁
        return Boolean.TRUE.equals (stringRedisTemplate.opsForValue ().setIfAbsent (LOCK_KEYPREFIX + name, threadId ,timeout, TimeUnit.SECONDS));
    }
    
    @Override
    public void unLock () {
        // 获取当前redis的🔒
        String currentLock = stringRedisTemplate.opsForValue ().get (LOCK_KEYPREFIX + name);
        // 该线程的锁id
        String threadId = ID_KEYPREFIX + Thread.currentThread ().threadId ();
        if (currentLock.equals (threadId)) {
            stringRedisTemplate.delete (LOCK_KEYPREFIX + name);
        }
        // 不一致不管 结束
    }
}
