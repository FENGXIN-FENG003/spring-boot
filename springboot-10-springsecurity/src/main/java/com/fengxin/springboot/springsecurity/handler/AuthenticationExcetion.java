package com.fengxin.springboot.springsecurity.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author FENGXIN
 * @date 2024/9/6
 * @project springboot-part
 * @description
 **/
public class AuthenticationExcetion implements AuthenticationEntryPoint {
    
    @Override
    public void commence (HttpServletRequest request , HttpServletResponse response , AuthenticationException authException) throws IOException, ServletException {
    
    }
}
