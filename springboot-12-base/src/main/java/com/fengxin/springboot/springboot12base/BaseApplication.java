package com.fengxin.springboot.springboot12base;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author FENGXIN
 * @date 2024/08/29
 */
@SpringBootApplication
public class BaseApplication {
    
    public static void main (String[] args) {
        /* // 1. SpringApplication 应用程序入口
        SpringApplication.run (BaseApplication.class , args); */
        
        /* // 2. 分开运行应用
        SpringApplication springApplication = new SpringApplication (BaseApplication.class);
        // 自定义时 配置文件优先级更高
        springApplication.setBannerMode (Banner.Mode.OFF);
        // ...
         springApplication.run (args); */
        
        // 3. FluentBuilder API
        new SpringApplicationBuilder (BaseApplication.class)
                .bannerMode (Banner.Mode.CONSOLE)
                // ...
                .run (args);
    }
    
}
