package com.fengxin.springboot.springsecurity.config;

import com.fengxin.springboot.springsecurity.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author FENGXIN
 * @date 2024/9/3
 * @project springboot-part
 * @description
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // @Autowired
    // private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    /**
     * 使用boot提供的加密工具
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder ();
    }
    
    /**
     * 配置过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests((authorizeHttpRequests) -> {
                    // 放行我自己的登录接口和swagger接口，不需要认证和授权
                    authorizeHttpRequests
                            .requestMatchers("/user/login")
                            .permitAll()
                            // 其他请求都需要授权后才能使用(如果不写会被过滤器拦截除上面配置请求的所有请求)
                            .anyRequest()
                            .authenticated();
                })
                // 关闭跨域
                .csrf(AbstractHttpConfigurer::disable)
                // 不通过session创建管理SecurityContextHolder
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
                // 添加自定义过滤器
                // .addFilterBefore (jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    /**
     * 配置AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // 调用自己的UserService方法 最终校验 返回前端响应
        return authenticationConfiguration.getAuthenticationManager ();
    }
}
