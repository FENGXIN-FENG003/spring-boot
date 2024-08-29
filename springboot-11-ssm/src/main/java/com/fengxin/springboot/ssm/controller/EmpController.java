package com.fengxin.springboot.ssm.controller;

import com.fengxin.springboot.ssm.pojo.Emp;
import com.fengxin.springboot.ssm.service.impl.EmpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/8/29
 * @project springboot-part
 * @description
 **/
@RestController
public class EmpController {
    @Autowired
    EmpServiceImpl empService;
    
    @GetMapping("/empid/{id}")
    public Emp queryById(@PathVariable("id") Long id){
        return empService.queryById (id);
    }
}
