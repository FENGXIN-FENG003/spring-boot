package com.fengxin.springboot.springsecurity.filter;

import com.alibaba.fastjson2.JSONObject;
import com.fengxin.springboot.springsecurity.service.impl.UserDetailsImpl;
import com.fengxin.springboot.springsecurity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author FENGXIN
 * @date 2024/9/4
 * @project springboot-part
 * @description jwt认证过滤器 解析token 存储认证信息
 **/
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    protected void doFilterInternal (HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader ("token");
        if (!StringUtils.hasText (token)) {
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
        String jsonUserDetails = stringRedisTemplate.opsForValue ().get ("login:user:" + userId);
        UserDetailsImpl userDetails = JSONObject.parseObject (jsonUserDetails , UserDetailsImpl.class);
        if (userDetails == null) {
            throw new RuntimeException ("用户未登录");
        }
        // 存储认证信息
        // TODO 存储权限
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken (userId , null , userDetails.getAuthorities ());
        SecurityContextHolder.getContext ().setAuthentication (authenticationToken);
        // 放行
        filterChain.doFilter (request , response);
    }
}
