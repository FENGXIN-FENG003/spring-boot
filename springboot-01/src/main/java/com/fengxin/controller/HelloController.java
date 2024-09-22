package com.fengxin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/8/6
 * @project springboot-part
 * @description
 **/
@RestController
@RequestMapping("hello")
public class HelloController {
    @GetMapping("boot")
    public String hello(){
        return "Hello SpringBoot!";
    }
}
