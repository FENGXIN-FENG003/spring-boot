package com.hmdp.utils;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author FENGXIN
 * @description 封装逻辑过期商品
 */
@Data
public class RedisData {
    private LocalDateTime expireTime;
    // 封装任意商品
    private Object data;
}
