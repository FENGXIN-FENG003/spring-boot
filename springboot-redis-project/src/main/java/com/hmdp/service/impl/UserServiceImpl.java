package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.SystemConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
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
        // 保存验证码到redis 并设置有效期两分钟
        stringRedisTemplate.opsForValue ().set (RedisConstants.LOGIN_CODE_KEY + phone,code,RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
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
        // 从redis校验验证码
        String code = stringRedisTemplate.opsForValue ().get (RedisConstants.LOGIN_CODE_KEY + loginFormPhone);
        if (code == null || !code.equals (loginForm.getCode ())){
            return Result.fail ("验证码错误");
        }
        // 校验成功 在数据库查询此用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<> ();
        queryWrapper.eq ("phone",loginFormPhone);
        User selecteUser = query ().getBaseMapper ().selectOne (queryWrapper);
        if (selecteUser == null){
            // 不存在 注册
            selecteUser = createUser (loginFormPhone);
            save (selecteUser);
        }
        // 保存dto到redis中
        // 1.生成token 未来前端访问都将携带 登录验证
        String token = String.valueOf (UUID.fastUUID ());
        // 2.将user转换为HashMap存储
        // 避免类型转换错误
        Map<String, Object> userMap = BeanUtil.beanToMap (
                BeanUtil.copyProperties (selecteUser, UserDTO.class)
                , new HashMap<> ()
                , CopyOptions.create ()
                        .setIgnoreNullValue (true)
                        .setFieldValueEditor (
                                /* setFieldValueEditor()：设置CopyOptions对象的一个属性，用于自定义属性值的转换方式。这里使用了lambda表达式，将属性值转换为字符串类型。
                                fieldName：表示当前要转换的属性名。
                                fieldValue：表示当前要转换的属性值。
                                fieldValue.toString()：将属性值转换为字符串类型。 */
                                (fieldName, fieldValue) -> fieldValue.toString ()));
        
        
        // 3.存储到redis
        stringRedisTemplate.opsForHash ().putAll (RedisConstants.LOGIN_USER_KEY + token,userMap);
        // 4.设置有效时间
        stringRedisTemplate.expire (RedisConstants.LOGIN_USER_KEY + token,RedisConstants.LOGIN_USER_TTL,TimeUnit.MINUTES);
        // redis保存时间是固定的 自开始就计算 不因为在此期间访问接口就刷新保存时间 因此在拦截器进行保存时间刷新
        // 返回token
        return Result.ok (token);
    }
    
    /**
     * 创建待存入的user
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
