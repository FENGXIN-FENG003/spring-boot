package com.hmdp.springboot.springsecurity.service;

import com.hmdp.springboot.springsecurity.pojo.User;
import com.hmdp.springboot.springsecurity.utils.ResponseResult;

/**
 * @author FENGXIN
 * @date 2024/9/4
 * @project springboot-part
 * @description
 **/
public interface LoginService {
    ResponseResult login (User user) throws Exception;
    
    ResponseResult logout ();
}
