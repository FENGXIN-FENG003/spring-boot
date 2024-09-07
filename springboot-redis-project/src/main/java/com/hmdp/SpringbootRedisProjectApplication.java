package com.hmdp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author FENGXIN
 */
@MapperScan("com.hmdp.mapper")
@SpringBootApplication
public class SpringbootRedisProjectApplication {
    
    public static void main (String[] args) {
        SpringApplication.run (SpringbootRedisProjectApplication.class , args);
    }
    
}
