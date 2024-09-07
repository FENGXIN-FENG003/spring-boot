package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.SystemConstants;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    
    /**
     * 发送验证码
     */
    @Override
    public Result sendCode (String phone , HttpSession session) {
        // 校验手机号
        if(RegexUtils.isPhoneInvalid (phone)){
            // 手机号无效 返回信息
            return Result.fail ("手机号无效");
        }
        
        // 校验通过 生成验证码
        String code = RandomUtil.randomNumbers (6);
        // 保存验证码到redis
        session.setAttribute (phone,code);
        // 返回验证码
        log.info ("验证码生成成功 {}",code);
        return Result.ok ();
    }
    
    /**
     * 登录功能
     */
    @Override
    public Result login (LoginFormDTO loginForm , HttpSession session) {
        // 校验手机号
        String loginFormPhone = loginForm.getPhone ();
        if (loginFormPhone == null || RegexUtils.isPhoneInvalid (loginFormPhone)){
            return Result.fail ("手机号错误");
        }
        // 校验验证码
        Object attribute = session.getAttribute (loginFormPhone);
        String sessionCode = attribute.toString ();
        if (attribute == null || !sessionCode.equals (loginForm.getCode ())){
            return Result.fail ("验证码错误");
        }
        // 校验成功 在数据库查询此用户
        User user = createUser (loginFormPhone);
        QueryWrapper<User> queryWrapper = new QueryWrapper<> ();
        queryWrapper.eq ("phone",loginFormPhone);
        User selecteUser = query ().getBaseMapper ().selectOne (queryWrapper);
        if (selecteUser == null){
            // 不存在 注册
            save (user);
        }
        // 保存dto到session中
        session.setAttribute ("user", BeanUtil.copyProperties (selecteUser, UserDTO.class));
        // 返回ok
        return Result.ok ();
    }
    
    /**
     * 创建待存入或查询的user
     */
    public User createUser(String loginFormPhone){
        User user = new User ();
        user.setPhone (loginFormPhone);
        user.setNickName (SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString (10));
        user.setCreateTime (LocalDateTime.now ());
        user.setUpdateTime (LocalDateTime.now ());
        return user;
    }
}
