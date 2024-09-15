package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FENGXIN
 * @date 2024/9/15
 * @project springboot-part
 * @description
 **/
@Configuration
public class RedissonConfig {
    @Value ("${spring.data.redis.host}")
    private String host;
    @Value ("${spring.data.redis.password}")
    private String pwd;
    @Value ("${spring.data.redis.port}")
    private String port;
    @Bean
    public RedissonClient redissonClient(){
        // 配置
        Config config = new Config ();
        config.useSingleServer ().setAddress ("redis://" + host + ":" + port).setPassword (pwd);
        // 创建redisson
        return Redisson.create (config);
    }
}
