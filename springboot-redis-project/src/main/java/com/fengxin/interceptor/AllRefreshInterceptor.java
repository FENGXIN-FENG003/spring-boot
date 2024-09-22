package com.fengxin.interceptor;

import cn.hutool.core.bean.BeanUtil;
import com.fengxin.dto.UserDTO;
import com.fengxin.utils.RedisConstants;
import com.fengxin.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author FENGXIN
 * @date 2024/9/7
 * @project springboot-part
 * @description 只做刷新redis时间 不拦截任何请求 优先级最高
 **/
public class AllRefreshInterceptor implements HandlerInterceptor {
    
    private StringRedisTemplate stringRedisTemplate;
    
    public AllRefreshInterceptor (StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    
    @Override
    public boolean preHandle (HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {
        // 1.获取token
        String token = request.getHeader ("authorization");
        if (!StringUtils.hasText (token)){
            // 不存在
            return true;
        }
        // 2.根据token获取userMap
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash ().entries (RedisConstants.LOGIN_USER_KEY + token);
        if (userMap == null) {
            // 不存在
            return true;
        }
        // 存在
        // 3.刷新有效时间
        stringRedisTemplate.expire (RedisConstants.LOGIN_USER_KEY + token,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 4.将map转换为userDTO
        UserDTO userDTO = BeanUtil.fillBeanWithMap (userMap , new UserDTO () , false);
        // 5.存在 存入ThreadLocal 放行
        UserHolder.saveUser (userDTO);
        return true;
    }
    
    @Override
    public void afterCompletion (HttpServletRequest request , HttpServletResponse response , Object handler , Exception ex) throws Exception {
        // 登出 移除用户
        UserHolder.removeUser ();
    }
}
