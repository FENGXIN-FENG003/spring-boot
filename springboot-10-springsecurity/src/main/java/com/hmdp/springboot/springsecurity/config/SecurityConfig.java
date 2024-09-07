package com.hmdp.springboot.springsecurity.config;

import com.hmdp.springboot.springsecurity.filter.JwtAuthenticationTokenFilter;
import com.hmdp.springboot.springsecurity.handler.AccessDeniedException;
import com.hmdp.springboot.springsecurity.handler.AuthenticationException;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * @author FENGXIN
 * @date 2024/9/3
 * @project springboot-part
 * @description
 **/
@Configuration
@EnableWebSecurity
// 配置授权
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    
    @Resource
    private AuthenticationException authenticationException;
    @Resource
    private AccessDeniedException accessDeniedException;
    
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
                    // 放行我自己的登录接口，不需要认证和授权
                    authorizeHttpRequests
                            .requestMatchers("/user/login")
                            .permitAll()
                            // 配置接口访问权限（在配置文件）
                            .requestMatchers ("/user/test")
                            .hasAuthority ("fx:dept:test")
                            // 其他请求都需要授权后才能使用(如果不写会被过滤器拦截除上面配置请求的所有请求)
                            .anyRequest()
                            .authenticated();
                })
                // 关闭跨域
                .csrf(AbstractHttpConfigurer::disable)
                // 不通过session创建管理SecurityContextHolder
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 添加自定义过滤器
                .addFilterBefore (jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling (exceptionHandling -> exceptionHandling.authenticationEntryPoint (authenticationException))
                .exceptionHandling (exceptionHandling -> exceptionHandling.accessDeniedHandler (accessDeniedException));
        
        return http.build();
    }
    /**
     * 配置AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // 调用自己的UserService方法 最终校验获取UserDetails 返回jwt前端响应
        return authenticationConfiguration.getAuthenticationManager ();
    }
    
    /**
     * 配置跨域
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration ();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        return request -> configuration;
    }
}
