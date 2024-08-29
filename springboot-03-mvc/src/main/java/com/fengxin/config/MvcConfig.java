package com.fengxin.config;

import com.fengxin.interceptor.Interceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/

@Configuration
// 不需要 @ComponentScan({""})
// 创建组件handlerAdapter handlerMapping 并给经理Adapter添加json转换器
// 不需要 @EnableWebMvc
// 实现接口WebMvcConfigurer 配置mvc组件 提供各种方法 重写即可使用 不用使用@Bean注解添加组件
public class MvcConfig implements WebMvcConfigurer {
    private final Interceptor interceptor;
    
    public MvcConfig (Interceptor interceptor) {
        this.interceptor = interceptor;
    }
    
    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor (interceptor);
    }
}