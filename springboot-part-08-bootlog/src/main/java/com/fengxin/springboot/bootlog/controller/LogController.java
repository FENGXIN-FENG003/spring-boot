package com.fengxin.springboot.bootlog.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/8/22
 * @project springboot-part
 * @description 日志使用
 **/
@Slf4j
@RestController
public class LogController {
    // 使用底层记录日志
    Logger logger = LoggerFactory.getLogger (getClass ());
    
    @GetMapping("/log")
    public String hello(){
        // 使用lombok的日志输出
        // log.info ("hello log...");
        logger.info ("hello log...");
        return "Hello BootLog";
    }
}
