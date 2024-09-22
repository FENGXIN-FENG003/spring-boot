package com.fengxin.springboot.springsecurity.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.fengxin.springboot.springsecurity.pojo.User;
import com.fengxin.springboot.springsecurity.service.LoginService;
import com.fengxin.springboot.springsecurity.utils.JwtUtil;
import com.fengxin.springboot.springsecurity.utils.ResponseResult;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Resource
    private StringRedisTemplate stringRedisTemplate;
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
        // 获取UserDetails 用户的信息都封装在这里
        UserDetailsImpl userDetails = (UserDetailsImpl)authenticate.getPrincipal ();
        // 根据userid生成jwt
        String id = userDetails.getUser ().getId ().toString ();
        String jwt = JwtUtil.createJWT (id);
        // json转换
        String jsonUserDetails = JSONObject.toJSONString (userDetails);
        // 将userDetails存入redis
        stringRedisTemplate.opsForValue ().set ("login:user:" + id, jsonUserDetails);
        // 响应前端
        Map<String, String> map = new HashMap<> ();
        map.put ("token", jwt);
        return new ResponseResult(200, "登录成功", map);
    }
    
    @Override
    public ResponseResult logout () {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext ().getAuthentication ();
        String id = (String) authentication.getPrincipal ();
        // 根据id删除redis中的token 删除之后redis无token 通过jwt过滤器时无法获取token 从而实现拦截
        stringRedisTemplate.delete ("login:user:" + id);
        return new ResponseResult (200,"退出登录成功");
    }
}
