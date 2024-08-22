package com.fengxin.springboot.springbootpart07bootplugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 */
@SpringBootApplication
// scanBasePackages属性可以指定扫描包路径 但是约定大于配置 一般不建议这样做
// @SpringBootConfiguration + @ComponentScan + @EnableAutoConfiguration = @SpringBootApplication
public class SpringbootPart07BootpluginApplication {
    
    public static void main (String[] args) {
        SpringApplication.run (SpringbootPart07BootpluginApplication.class , args);
    }
    
}
