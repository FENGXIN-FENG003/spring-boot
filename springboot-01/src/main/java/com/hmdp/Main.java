package com.hmdp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 * @date 2024/8/6
 * @project springboot-part
 * @description 启动类 其他层架构在启动类同/子包下即可生效
 **/
/*
    @SpringBootConfiguration
    @EnableAutoConfiguration
    @ComponentScan
    本身是配置类
    自动加载配置
    扫描组件
    组件可以加在这里
 */
@SpringBootApplication
public class Main {
    public static void main (String[] args) {
        // 创建ioc容器 加载配置
        // 启动web服务器tomcat
        SpringApplication.run (Main.class,args);
    }
}
