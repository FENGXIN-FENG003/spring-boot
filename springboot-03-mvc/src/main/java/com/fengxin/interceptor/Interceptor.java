package com.fengxin.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description 自定义拦截器
 **/
@Component
public class Interceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println ("request = " + request + ", response = " + response + ", handler = " + handler);
        return true;
    }
}
