package com.hmdp.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author FENGXIN
 * @date 2024/9/13
 * @project springboot-part
 * @description 生成全局唯一id 处理分布式高并发秒杀订单商品id
 **/
@Component
public class RedisGenerateId {
    /**
     * 初始时间
     */
    private static final long TIMESTAMP_BEGIN = 1704067200L;
    private static final int COUNT_BIT = 32;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public long generateId(String keyPrefix){
        // 生成时间戳
        // 用当前时间 - 初始时间获取时间戳
        LocalDateTime now = LocalDateTime.now ();
        long nowEpochSecond = now.toEpochSecond (ZoneOffset.UTC);
        long stamp = nowEpochSecond - TIMESTAMP_BEGIN;
        
        // 生成自增序列号
        // redis自增限制是2^64 如果只用一个初始key自增所有订单商品 会达到无法估量的数量 因此以每天划分 每天从新的初始key自增 还可以统计年月日的销售量
        // 自定义日期格式
        String date = now.format (DateTimeFormatter.ofPattern ("yyyyMMdd"));
        long increment = stringRedisTemplate.opsForValue ().increment ("icr:" + keyPrefix + ":" + date);
        
        // 拼接id 时间戳32bit 自增序列32bit 将时间戳左移32bit 再加入自增序列（使用或运算 000000... | increment = increment）
        return stamp << COUNT_BIT | increment;
    }
    
    // /**
    //  * 生成初始时间戳
    //  */
    // public static void main (String[] args) {
    //     LocalDateTime beginTime = LocalDateTime.of (2024 , 1 , 1 , 0 , 0 , 0);
    //     long epochSecond = beginTime.toEpochSecond (ZoneOffset.UTC);
    //     System.out.println ("epochSecond = " + epochSecond);
    // }
}
