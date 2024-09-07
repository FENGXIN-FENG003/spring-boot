package com.hmdp.springboot.springsecurity.handler;

import com.alibaba.fastjson2.JSONObject;
import com.hmdp.springboot.springsecurity.utils.ResponseResult;
import com.hmdp.springboot.springsecurity.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author FENGXIN
 * @date 2024/9/6
 * @project springboot-part
 * @description 认证失败处理
 **/
@Component
public class AuthenticationException implements AuthenticationEntryPoint {
    
    @Override
    public void commence (HttpServletRequest request , HttpServletResponse response , org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        ResponseResult responseResult = new ResponseResult (HttpStatus.FORBIDDEN.value () , "认证失败");
        String jsonString = JSONObject.toJSONString (responseResult);
        WebUtils.renderString ( response , jsonString);
    }
}
