package com.hmdp.springboot.bootweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @author FENGXIN
 * @date 2024/8/24
 * @project springboot-part
 * @description @Configuration + WebMvcConfigurer 在原有配置的基础下，自定义添加配置
 **/
@Configuration
@EnableWebMvc
public class MvcConfig /*implements WebMvcConfigurer*/ {
    // /**
    //  * 1. implements WebMvcConfigurer
    //  * 基于配置类自定义静态资源
    //  * @param registry registry
    //  */
    // @Override
    // public void addResourceHandlers (ResourceHandlerRegistry registry) {
    //     WebMvcConfigurer.super.addResourceHandlers (registry);
    //     registry.addResourceHandler ("/static/**")
    //             .addResourceLocations ("classpath:/a/","classpath:/b/")
    //             .setCacheControl (CacheControl.maxAge (7200, TimeUnit.SECONDS));
    // }
    
    /**
     * 2. 使用组件配置
     * @return WebMvcConfigurer
     * 为什么使用bean组件也能实现自定义配置？
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer () {
            @Override
            public void addResourceHandlers (ResourceHandlerRegistry registry) {
                WebMvcConfigurer.super.addResourceHandlers (registry);
                registry.addResourceHandler ("/static/**")
                        .addResourceLocations ("classpath:/a/","classpath:/b/")
                        .setCacheControl (CacheControl.maxAge (7200, TimeUnit.SECONDS));
            }
        };
    }
}
