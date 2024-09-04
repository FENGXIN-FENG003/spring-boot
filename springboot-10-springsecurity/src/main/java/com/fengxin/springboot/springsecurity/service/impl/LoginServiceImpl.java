package com.fengxin.springboot.springsecurity.service.impl;

import com.fengxin.springboot.springsecurity.pojo.User;
import com.fengxin.springboot.springsecurity.service.LoginService;
import com.fengxin.springboot.springsecurity.utils.JwtUtil;
import com.fengxin.springboot.springsecurity.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FENGXIN
 * @date 2024/9/4
 * @project springboot-part
 * @description
 **/
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    
    @Override
    public ResponseResult login (User user) throws Exception {
        // 调用 AuthenticationManager 的 authenticate 方法进行认证
        Authentication authentication = new UsernamePasswordAuthenticationToken (user.getUserName (), user.getPassword ());
        Authentication authenticate = authenticationManager.authenticate (authentication);
        // 认证失败，抛异常
        if(authenticate == null){
            throw new Exception ("用户名或密码错误");
        }
        // 认证成功后，生成jwt 存入redis
        UserDetailsImpl userDetails = (UserDetailsImpl)authenticate.getPrincipal ();
        // 根据userid生成jwt
        String id = userDetails.getUser ().getId ().toString ();
        String jwt = JwtUtil.createJWT (id);
        // 将jwt存入redis
        // 响应前端
        Map<String, String> map = new HashMap<> ();
        map.put ("token", jwt);
        return new ResponseResult(200, "登录成功", map);
    }
}
