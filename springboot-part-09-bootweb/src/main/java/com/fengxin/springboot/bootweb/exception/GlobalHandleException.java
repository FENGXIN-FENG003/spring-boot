package com.fengxin.springboot.bootweb.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author FENGXIN
 * @date 2024/8/27
 * @project springboot-part
 * @description
 **/
// 添加注解 @RestControllerAdvice 或者 @ControllerAdvice
@RestControllerAdvice
public class GlobalHandleException {
    /**
     * 异常逻辑
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exception(Exception e){
        return "全局异常处理" + e.getMessage ();
    }
}
