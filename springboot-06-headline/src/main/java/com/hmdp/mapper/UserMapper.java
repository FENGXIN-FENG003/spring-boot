package com.hmdp.mapper;

import com.hmdp.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author FENGXIN
* @description 针对表【news_user】的数据库操作Mapper
* @createDate 2024-08-12 13:33:39
* @Entity com.hmdp.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}



