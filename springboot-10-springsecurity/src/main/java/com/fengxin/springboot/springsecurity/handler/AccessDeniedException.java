package com.fengxin.springboot.springsecurity.handler;

import com.alibaba.fastjson2.JSONObject;
import com.fengxin.springboot.springsecurity.utils.ResponseResult;
import com.fengxin.springboot.springsecurity.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author FENGXIN
 * @date 2024/9/6
 * @project springboot-part
 * @description 权限不足异常处理
 **/
@Component
public class AccessDeniedException implements AccessDeniedHandler {
    
    @Override
    public void handle (HttpServletRequest request , HttpServletResponse response , org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult responseResult = new ResponseResult (HttpStatus.UNAUTHORIZED.value () , "权限不足，请联系管理员");
        String jsonString = JSONObject.toJSONString (responseResult);
        WebUtils.renderString ( response , jsonString);
        
    }
}
