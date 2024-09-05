package com.fengxin.springboot.springsecurity.service;

import com.fengxin.springboot.springsecurity.pojo.User;
import com.fengxin.springboot.springsecurity.utils.ResponseResult;

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
