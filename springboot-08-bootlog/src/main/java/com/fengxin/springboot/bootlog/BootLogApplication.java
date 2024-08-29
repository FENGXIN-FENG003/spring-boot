package com.fengxin.springboot.bootlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 * @createDate 2024/8/22 18:35:33
 * @description boot底层默认日志门面（类似接口）：SLF4J 日志实现：LogBack 相关默认输出配置在spring-boot:additional-spring-configuration-metadata.json
 */
@SpringBootApplication
public class BootLogApplication {
    
    public static void main (String[] args) {
        SpringApplication.run (BootLogApplication.class , args);
    }
    
}
