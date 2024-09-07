package com.hmdp.springboot.springsecurity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 * @date 2024/08/26
 */
@SpringBootApplication
@MapperScan("com.hmdp.springboot.springsecurity.mapper")
public class SpringSecurityApplication {
    
    public static void main (String[] args) {
        SpringApplication.run (SpringSecurityApplication.class , args);
    }
    
}
