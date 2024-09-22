package com.fengxin.springboot.bootweb.service.impl;

import com.fengxin.springboot.bootweb.service.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author FENGXIN
 * @date 2024/8/28
 * @project springboot-part
 * @description
 **/
public class PersonServiceImpl implements PersonService {
    /**
     * 请求上下文过滤器
     */
    @Override
    public void requestAndResponse () {
        // 获取当前请求和响应信息封装体
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes ();
        
        // 获取请求和响应信息
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest ();
        }
        if (attributes != null) {
            HttpServletResponse response = attributes.getResponse ();
        }
        // ...
    }
}
