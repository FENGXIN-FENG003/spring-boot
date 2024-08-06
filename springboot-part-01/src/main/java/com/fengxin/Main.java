package com.fengxin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author FENGXIN
 * @date 2024/8/6
 * @project springboot-part
 * @description 启动类 其他层架构在启动类同/子包下即可生效
 **/
@SpringBootApplication
public class Main {
    public static void main (String[] args) {
        SpringApplication.run (Main.class,args);
    }
}
