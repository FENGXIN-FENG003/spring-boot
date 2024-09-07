package com.hmdp.controller;

import com.hmdp.pojo.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description yml文件的使用
 **/
@RestController
@RequestMapping("/yml")
public class AppController {
    @Autowired
    private DataSource dataSource;
    @GetMapping("/hello")
    public String hello() {
        System.out.println (dataSource);
        return "hello mvc";
    }
}
