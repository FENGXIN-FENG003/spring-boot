package com.hmdp.config;

import com.hmdp.interceptor.HeadlineInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FENGXIN
 * @date 2024/8/14
 * @project springboot-part
 * @description
 **/

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Resource
    private HeadlineInterceptor headlineInterceptor;
    
    // 配置拦截器
    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        // 指定要拦截的地方
        registry.addInterceptor (headlineInterceptor).addPathPatterns ("/headline/**");
    }
}