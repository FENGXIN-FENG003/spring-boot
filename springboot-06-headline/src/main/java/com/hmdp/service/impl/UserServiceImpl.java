package com.hmdp.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.pojo.User;
import com.hmdp.service.UserService;
import com.hmdp.mapper.UserMapper;
import com.hmdp.util.JwtHelper;
import com.hmdp.util.MD5Util;
import com.hmdp.util.Result;
import com.hmdp.util.ResultCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
* @author FENGXIN
* @description 针对表【news_user】的数据库操作Service实现
* @createDate 2024-08-12 13:33:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private JwtHelper jwtHelper;
    
    /**
     * 登录校验
     * @param user 前端请求的数据
     * @return result
     */
    @Override
    public Result<Map<String,String>> login (User user) {
        // 根据name查询user
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User user1 = userMapper.selectOne (wrapper);
        
        // 如果user == null
        if (user1 == null) {
            System.out.println ("user is null");
            return Result.build (null,ResultCodeEnum.USERNAME_ERROR);
        }
        
        // 校验密码
        if (!StringUtils.isEmpty (user.getUserPwd ()) && MD5Util.encrypt (user.getUserPwd ()).equals(user1.getUserPwd())) {
            // 成功 生成token返回
            Map<String,String> map = new HashMap<> ();
            map.put ("token",jwtHelper.createToken (Long.valueOf (user1.getUid ())));
            System.out.println ("success login and map = " + map);
            return Result.ok (map);
        }
        
        // 校验失败
        System.out.println ("password error");
        return Result.build (null,ResultCodeEnum.PASSWORD_ERROR);
    }
    
    /**
     * 响应用户请求 返回用户信息
     */
    @Override
    public Result userInfo (String token) {
        // 校验有效期
        if (jwtHelper.isExpiration (token)) {
            return Result.build (null,ResultCodeEnum.NOTLOGIN);
        }
        
        // 解析用户id
        Long uid = jwtHelper.getUserId (token);
        
        // 获取数据库用户信息
        User user = userMapper.selectById (uid);
        
        // 响应用户信息
        if (user == null) {
            System.out.println ("user is null");
            return Result.build (null,ResultCodeEnum.NOTLOGIN);
        }
        
        user.setUserPwd ("");
        Map<String,User> map = new HashMap<> ();
        map.put("loginUser",user);
        System.out.println ("loginUser = " + map);
        return Result.ok (map);
        
    }
    
    /**
     * 校验用户注册名
     */
    @Override
    public Result checkUserName (String username) {
        
        // 根据username查询数据库
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq (User::getUsername,username);
        User registerUser = userMapper.selectOne (wrapper);
        
        // 存在此用户
        if (registerUser != null) {
            
            return  Result.build ("",ResultCodeEnum.USERNAME_USED);
        }
        
        // 不存在重复用户
        return Result.ok (null);
    }
    
    /**
     * 用户注册
     */
    @Override
    public Result register (User user) {
        // 校验用户注册名
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq (User::getUsername,user.getUsername());
        User user1 = userMapper.selectOne (wrapper);
        if (user1 != null) {
            return Result.build (null,ResultCodeEnum.USERNAME_USED);
        }
        
        // 注册成功 存入数据库 响应前端
        if (userMapper.insert (user) > 0) {
            wrapper.eq (User::getUsername,user.getUsername());
            System.out.println ("success insert user : " + userMapper.selectOne (wrapper));
        }
        return Result.ok (null);
    }
    
    /**
     * 根据token校验登录是否过期
     */
    @Override
    public Result checkLogin (String token) {
        if(jwtHelper.isExpiration (token)) {
            return Result.build (null, ResultCodeEnum.NOTLOGIN);
        }
        return Result.ok (null);
    }
}




