package com.fengxin.springboot.springsecurity.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxin.springboot.springsecurity.pojo.User;
import com.fengxin.springboot.springsecurity.service.UserService;
import com.fengxin.springboot.springsecurity.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author FENGXIN
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-08-30 20:13:11
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




