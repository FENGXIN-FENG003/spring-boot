package com.fengxin.springboot.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/8/28
 * @project springboot-part
 * @description
 **/
@RestController
public class HelloController {
    
    @GetMapping("/hello")
    // 权限
    @PreAuthorize ("hasAuthority('hello')")
    public String hello(){
        return "hello";
    }
}
