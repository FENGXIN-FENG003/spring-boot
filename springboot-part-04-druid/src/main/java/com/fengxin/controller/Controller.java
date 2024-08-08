package com.fengxin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
@RestController
@RequestMapping("/d")
public class Controller {
    @GetMapping("hello")
    public String hello() {
        return "hello druid";
    }
}
