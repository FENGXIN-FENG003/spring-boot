package com.fengxin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author FENGXIN
 */

@MapperScan("com.fengxin.mapper")
@SpringBootApplication
// 开启AspectJ的自动代理
@EnableAspectJAutoProxy(exposeProxy = true)
public class SpringbootRedisProjectApplication {
    
    public static void main (String[] args) {
        SpringApplication.run (SpringbootRedisProjectApplication.class , args);
    }
    
}
