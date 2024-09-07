package com.hmdp.springboot.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author FENGXIN
 * @date 2024/9/6
 * @project springboot-part
 * @description
 **/
@Configuration
@CrossOrigin
public class CorConfig implements WebMvcConfigurer {

}
