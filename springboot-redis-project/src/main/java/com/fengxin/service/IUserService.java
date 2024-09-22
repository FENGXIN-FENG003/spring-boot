package com.fengxin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxin.dto.LoginFormDTO;
import com.fengxin.dto.Result;
import com.fengxin.entity.User;
import jakarta.servlet.http.HttpSession;

/**
 * @author FENGXIN
 */
public interface IUserService extends IService<User> {
    
    Result sendCode (String phone , HttpSession session);
    
    Result login (LoginFormDTO loginForm , HttpSession session);
    
    Result sign ();
    
    Result signCount ();
    
}
