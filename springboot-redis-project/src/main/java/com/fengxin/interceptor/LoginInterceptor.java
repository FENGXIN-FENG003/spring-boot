package com.fengxin.interceptor;

import com.fengxin.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author FENGXIN
 * @date 2024/9/7
 * @project springboot-part
 * @description 登录用户校验拦截
 **/
public class LoginInterceptor implements HandlerInterceptor {
    
    
    @Override
    public boolean preHandle (HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
        if (UserHolder.getUser () == null) {
            // 拦截
            response.setStatus (HttpStatus.UNAUTHORIZED.value ());
            return false;
        }
        return true;
    }
}
