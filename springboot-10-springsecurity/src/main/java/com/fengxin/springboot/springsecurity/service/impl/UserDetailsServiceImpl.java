package com.fengxin.springboot.springsecurity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengxin.springboot.springsecurity.mapper.UserMapper;
import com.fengxin.springboot.springsecurity.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/3
 * @project springboot-part
 * @description 自定义UserDetailsService 从数据库校验用户登陆
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    // 注入UserMapper
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        // 查询数据
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq (User::getUserName , username);
        User user = userMapper.selectOne (queryWrapper);
        // 异常处理 将会在事务中处理异常
        if (user == null) {
            throw new UsernameNotFoundException ("用户名不存在！");
        }
        // TODO 获取权限 封装
        List<String> authoritiesList = new ArrayList<> (List.of ("test","hello"));
        // 返回UserDetails的实现类
        return new UserDetailsImpl (user,authoritiesList);
    }
}
