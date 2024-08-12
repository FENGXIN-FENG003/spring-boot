package com.fengxin.controller;

import com.fengxin.pojo.User;
import com.fengxin.service.UserService;
import com.fengxin.util.Result;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

/**
 * @author FENGXIN
 * @date 2024/8/12
 * @project springboot-part
 * @description
 **/
@RestController()
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Resource
    private UserService userService;
    
    // 用户登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        System.out.println ("user = " + user);
        return userService.login(user);
    }
    
    // 根据用户请求头token返回用户信息给客户端
    @GetMapping("/userInfo")
    public Result getUserInfo(@RequestHeader String token) {
        return userService.userInfo(token);
    }
    
    // 校验用户注册名
    @GetMapping("/checkUserName")
    public Result checkUserName(@Param ("username") String username) {
        return userService.checkUserName(username);
    }
    
    // 用户注册
    @PostMapping("/regist")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }
}
