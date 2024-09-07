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
    // @PreAuthorize ("hasAuthority('fx:dept:manager1')")
    // @PreAuthorize ("hasAnyAuthority('fx:dept:manager1','fx:dept:manager2')")
    // @PreAuthorize ("hasAuthority('ROLE_admin')")
    // @PreAuthorize ("hasAnyRole('admin','manager')")
    // 调用自定义权限校验
    @PreAuthorize ("@FXAuthority.hasAuthority('fx:dept:manager1')")
    public String hello(){
        return "hello";
    }
}
