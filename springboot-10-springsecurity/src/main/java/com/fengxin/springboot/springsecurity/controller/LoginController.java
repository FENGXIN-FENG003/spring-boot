package com.fengxin.springboot.springsecurity.controller;

import com.fengxin.springboot.springsecurity.pojo.User;
import com.fengxin.springboot.springsecurity.service.LoginService;
import com.fengxin.springboot.springsecurity.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/9/4
 * @project springboot-part
 * @description 登陆实现 需要告诉security放行此接口
 **/
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) throws Exception {
        return loginService.login(user);
    }
    
    @RequestMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
