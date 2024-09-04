package com.fengxin.springboot.springsecurity.filter;

import com.fengxin.springboot.springsecurity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author FENGXIN
 * @date 2024/9/4
 * @project springboot-part
 * @description jwt认证过滤器 解析token 存储认证信息
 **/
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal (HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader ("token");
        if (token != null) {
            // 放行 最后会有拦截器进一步校验
            filterChain.doFilter (request , response);
            // 防止过滤器链返回时再次进行不必要的解析
            return;
        }
        // 解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT (token);
            userId = claims.getSubject ();
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
        // 从redis获取
        // String redisToken = redisTemplate.opsForValue ().get (userId);
        // if (redisToken == null) {
        //     throw new RuntimeException ("用户未登录");
        // }
        // 存储认证信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken (userId , null , null);
        SecurityContextHolder.getContext ().setAuthentication (authenticationToken);
        // 放行
        filterChain.doFilter (request , response);
    }
}
