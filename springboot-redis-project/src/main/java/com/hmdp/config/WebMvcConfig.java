package com.hmdp.config;

import com.hmdp.interceptor.AllRefreshInterceptor;
import com.hmdp.interceptor.LoginInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FENGXIN
 * @date 2024/9/7
 * @project springboot-part
 * @description
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor (new LoginInterceptor())
                .excludePathPatterns (
                        "/user/login"
                        ,"/user/code"
                        ,"/shop"
                        ,"/shop/**"
                        ,"/voucher/**"
                        ,"/voucher-order/**"
                        ,"/blog/**"
                        ,"/user/sign"
                ).order (1);
        registry.addInterceptor (new AllRefreshInterceptor (stringRedisTemplate)).addPathPatterns ("/**")
                .order (0);
    }
}
