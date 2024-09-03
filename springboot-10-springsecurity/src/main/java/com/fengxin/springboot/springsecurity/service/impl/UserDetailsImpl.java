package com.fengxin.springboot.springsecurity.service.impl;

import com.fengxin.springboot.springsecurity.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

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
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        return null;
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
