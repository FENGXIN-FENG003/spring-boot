package com.fengxin.springboot.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author FENGXIN
 * @date 2024/9/3
 * @project springboot-part
 * @description
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * 使用boot提供的加密工具
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder ();
    }
}
