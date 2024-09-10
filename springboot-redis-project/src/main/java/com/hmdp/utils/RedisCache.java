package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author FENGXIN
 * @date 2024/9/10
 * @project springboot-part
 * @description 封装redis工具类
 **/
@Slf4j
@Component
public class RedisCache {
    
    private StringRedisTemplate stringRedisTemplate;
    
    public RedisCache (StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    
    /**
     * 保存数据到redis 并设置有效时间
     */
    public <T> void set(String keyPrefix, T id, Object value, Long time, TimeUnit timeUnit){
        String key = keyPrefix + id;
        stringRedisTemplate.opsForValue ().set (key, JSONObject.toJSONString (value),time,timeUnit);
    }
    
    /**
     * 保存数据到redis 并设置逻辑过期时间
     */
    public <T> void setLogicTime(String keyPrefix, T id, Object value, Long time, TimeUnit timeUnit){
        String key = keyPrefix + id;
        RedisData redisData = new RedisData ();
        redisData.setData (value);
        redisData.setExpireTime (LocalDateTime.now ().plusSeconds (timeUnit.toSeconds (time)));
        stringRedisTemplate.opsForValue ().set (key,JSONObject.toJSONString (redisData));
    }
    
    /**
     * 解决缓存穿透
     */
    public <R,T> R getDataThrough(String keyPrefix, T id, Class<R> type, Function<T,R> doFallback,Long time, TimeUnit timeUnit){
        String shopId = keyPrefix + id;
        // 1 先在redis查找缓存
        String shopData = stringRedisTemplate.opsForValue ().get (shopId);
        // 2 存在字符串 返回数据
        if (StringUtils.hasText (shopData)) {
            // 转换为对象并返回
            return JSONObject.parseObject (shopData,type);
        }
        // 存在字符串 但是id为“”（redis存储的空数据为""）
        if (Objects.equals (shopData , "")){
            // 直接返回错误信息
            return null;
        }
        // 3 不存在
        // 3.1 查询数据库
        R shopById = doFallback.apply (id);
        // 3.2 不存在 返回错误信息
        if (shopById == null) {
            // 防止缓存穿透 将空值写入redis
            stringRedisTemplate.opsForValue ().set (shopId, "", time,timeUnit);
            return null;
        }
        // 3.3 存在 将数据存入redis 并设置有效时间 手动实现数据库和缓存的数据一致性
        this.set (keyPrefix,id,shopById,time,timeUnit);
        // 3.4 返回数据
        return shopById;
    }
    
    // 线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool (10);
    /**
     * 基于逻辑过期解决缓存击穿
     */
    public <R,T> R logicSolvePunchThrough(String keyPrefix,T id,Class<R> type,Function<T,R> doFallback,Long time, TimeUnit timeUnit){
        String shopId = keyPrefix + id;
        // 1 先在redis查找缓存
        String shopData = stringRedisTemplate.opsForValue ().get (shopId);
        // 2 不存在
        // 因为已经缓存预热商品 逻辑过期 所以商品会一直存在缓存 不会消失 除非手动删除
        // 因此不存在就是商品不在活动里 直接返回null
        if (!StringUtils.hasText (shopData)) {
            return null;
        }
        
        // 存在 转换为对象
        RedisData redisData = JSONObject.parseObject (shopData , RedisData.class);
        // 获取商品
        JSONObject data = (JSONObject) redisData.getData ();
        R shop = JSONUtil.toBean (data.toJSONString () , type);
        // 是否过期
        // 未过期
        if(redisData.getExpireTime ().isAfter (LocalDateTime.now ())){
            return shop;
        }
        // 过期
        // 获取🔒
        if (tryLock (id)) {
            // 获取成功
            // 二次判断 是否过期 如果已经有线程更新了缓存则直接返回数据
            if (redisData.getExpireTime ().isAfter (LocalDateTime.now ())) {
                return shop;
            }
            // 开启新线程重建缓存
            CACHE_REBUILD_EXECUTOR.submit (() -> {
                try {
                    // 查询数据库
                    R byId = doFallback.apply (id);
                    // 存入redis
                    this.setLogicTime (keyPrefix,id,byId,time,timeUnit);
                    log.info ("存入redis数据成功");
                } catch (Exception e) {
                    throw new RuntimeException (e);
                } finally {
                    // 释放锁
                    delLock (id);
                }
            });
        }
        // 返回旧缓存 不再等待
        return shop;
    }
    
    /**
     * 获取🔒
     * @param id 商户id
     * @return boolean
     */
    public <T> boolean tryLock(T id){
        // 获取🔒
        // 返回🔒设置成功的标记 存在了就获取失败
        Boolean flag = stringRedisTemplate.opsForValue ().setIfAbsent (RedisConstants.LOCK_SHOP_KEY + id , "1" , RedisConstants.LOCK_SHOP_TTL , TimeUnit.SECONDS);
        return BooleanUtil.isTrue (flag);
    }
    
    /**
     * 释放🔒
     * @param id 商户id
     */
    public <T> void delLock(T id){
        // 释放🔒
        // 数据库查询数据成功 缓存更新成功 释放🔒
        stringRedisTemplate.delete (RedisConstants.LOCK_SHOP_KEY + id);
    }
    
}
