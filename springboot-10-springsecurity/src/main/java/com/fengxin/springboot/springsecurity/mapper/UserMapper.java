package com.fengxin.springboot.springsecurity.mapper;

import com.fengxin.springboot.springsecurity.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author FENGXIN
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-08-30 20:13:11
* @Entity com.pojo.springsecurity.springboot.fengxin.User
*/

public interface UserMapper extends BaseMapper<User> {
    List<String> selectPermByUserId(Long id);
}




