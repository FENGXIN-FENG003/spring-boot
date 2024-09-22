package com.fengxin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
// 指定mapper接口
@MapperScan("com.fengxin.mapper")
@SpringBootApplication
public class AppMybatis {
    public static void main (String[] args) {
        SpringApplication.run(AppMybatis.class, args);
    }
}
