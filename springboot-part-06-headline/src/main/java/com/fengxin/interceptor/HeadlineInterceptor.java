package com.fengxin.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fengxin.util.JwtHelper;
import com.fengxin.util.Result;
import com.fengxin.util.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author FENGXIN
 * @date 2024/8/14
 * @project springboot-part
 * @description 拦截器 如果token过期则禁止相关发布操作
 **/
@Component
public class HeadlineInterceptor implements HandlerInterceptor {
    
    @Resource
    private JwtHelper jwtHelper;
    
    /**
     * 拦截headline操作
     */
    @Override
    public boolean preHandle (HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
        // 根据token检查
        String token = request.getHeader("token");
        // 未过期
        if(!jwtHelper.isExpiration (token)) {
           return true;
        }
        // 过期 返回json用以前端阻止相关操作
        Result result = Result.build (null, ResultCodeEnum.NOTLOGIN);
        // 转换对为json字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString (result);
        // 写入response 返回前端
        response.getWriter().write(value);
        return true;
    }
}
