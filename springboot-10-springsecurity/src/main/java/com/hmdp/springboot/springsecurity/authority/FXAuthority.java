package com.hmdp.springboot.springsecurity.authority;

import com.hmdp.springboot.springsecurity.service.impl.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/7
 * @project springboot-part
 * @description
 **/
@Component
public class FXAuthority {
    
    /**
     * 判断用户是否具有某个权限
     */
    public final boolean hasAuthority(String authority){
        // 获取权限信息
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext ()
                .getAuthentication ()
                .getPrincipal ();
        List<String> permissions = userDetails.getPermissions ();
        // 校验权限
        return permissions.contains (authority);
    }
}
