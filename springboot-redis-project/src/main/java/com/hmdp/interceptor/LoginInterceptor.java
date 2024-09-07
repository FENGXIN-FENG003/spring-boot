package com.hmdp.interceptor;

import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.management.relation.RelationSupport;

/**
 * @author FENGXIN
 * @date 2024/9/7
 * @project springboot-part
 * @description 登录用户校验拦截
 **/
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle (HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
        // 获取session
        HttpSession session = request.getSession ();
        // 获取用户
        UserDTO user = (UserDTO) session.getAttribute ("user");
        // 校验用户是否存在
        if (user == null){
            // 不存在
            response.setStatus (HttpStatus.UNAUTHORIZED.value ());
            return false;
        }
        // 存在 存入ThreadLocal 放行
        UserHolder.saveUser (user);
        return true;
    }
    
    @Override
    public void afterCompletion (HttpServletRequest request , HttpServletResponse response , Object handler , Exception ex) throws Exception {
        // 登出 移除用户
        UserHolder.removeUser ();
    }
}
