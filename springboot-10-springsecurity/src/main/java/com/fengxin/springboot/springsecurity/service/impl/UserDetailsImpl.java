package com.fengxin.springboot.springsecurity.service.impl;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fengxin.springboot.springsecurity.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/3
 * @project springboot-part
 * @description UserDetails实现类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private User user;
    // 存储权限信息
    private List<String> permissions;
    // 存储权限信息 将来从这里获取权限
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities = new ArrayList<> ();
    
    public UserDetailsImpl (User user , List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }
    
    /**
     * SpringSecurity将在这里获取权限
     * @return 存储GrantedAuthority的collection GrantedAuthority存储权限字符
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        for (String permission : permissions) {
            // SimpleGrantedAuthority implements GrantedAuthority
            // permission存入SimpleGrantedAuthority 将来SpringSecurity从这里获取权限
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority (permission);
            authorities.add (simpleGrantedAuthority);
        }
        return authorities;
    }
    
    @Override
    public String getPassword () {
        return user.getPassword ();
    }
    
    @Override
    public String getUsername () {
        return user.getUserName ();
    }
    
    @Override
    public boolean isAccountNonExpired () {
        return UserDetails.super.isAccountNonExpired ();
    }
    
    @Override
    public boolean isAccountNonLocked () {
        return UserDetails.super.isAccountNonLocked ();
    }
    
    @Override
    public boolean isCredentialsNonExpired () {
        return UserDetails.super.isCredentialsNonExpired ();
    }
    
    @Override
    public boolean isEnabled () {
        return UserDetails.super.isEnabled ();
    }
}
