package com.fengxin.springboot.base;

import com.fengxin.springboot.base.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author FENGXIN
 * @date 2024/08/29
 */
@Slf4j
@SpringBootApplication
public class BaseApplication {
    
    public static void main (String[] args) {
        // 1. SpringApplication 应用程序入口
        ConfigurableApplicationContext ioc = SpringApplication.run (BaseApplication.class , args);
        
        /* // 2. 分开运行应用
        SpringApplication springApplication = new SpringApplication (BaseApplication.class);
        // 自定义时 配置文件优先级更高
        springApplication.setBannerMode (Banner.Mode.OFF);
        // ...
         springApplication.run (args); */
        
        /* // 3. FluentBuilder API
        new SpringApplicationBuilder (BaseApplication.class)
                .bannerMode (Banner.Mode.CONSOLE)
                // ...
                .run (args); */
        System.out.println ("-------------");
        // 测试激活环境
        try {
            Dev dev = ioc.getBean (Dev.class);
            System.out.println (dev);
        } catch (BeansException e) {}
        try {
            Default aDefault = ioc.getBean (Default.class);
            System.out.println (aDefault);
        }catch (Exception e){}
        try {
            Prod prod = ioc.getBean (Prod.class);
            System.out.println (prod);
        }catch (Exception e){}
        try {
            Test test = ioc.getBean (Test.class);
            System.out.println (test);
        }catch (Exception e){}
        try {
            Any any = ioc.getBean (Any.class);
            System.out.println (any);
        }catch (Exception e){}
    }
    
}
