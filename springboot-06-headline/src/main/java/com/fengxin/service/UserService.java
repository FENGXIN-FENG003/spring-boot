package com.fengxin.service;

import com.fengxin.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxin.util.Result;

/**
* @author FENGXIN
* @description 针对表【news_user】的数据库操作Service
* @createDate 2024-08-12 13:33:39
*/
public interface UserService extends IService<User> {
    
    Result login (User user);
    
    Result userInfo (String token);
    
    Result checkUserName (String username);
    
    Result register (User user);
    
    Result checkLogin (String token);
}
