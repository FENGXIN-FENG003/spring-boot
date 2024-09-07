package com.hmdp.springboot.ssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 * @date 2024/08/29
 */
// 扫描mapper接口
@MapperScan("com.hmdp.springboot.ssm.mapper")
@SpringBootApplication
public class SsmApplication {
    
    public static void main (String[] args) {
        SpringApplication.run (SsmApplication.class , args);
    }
    
}
