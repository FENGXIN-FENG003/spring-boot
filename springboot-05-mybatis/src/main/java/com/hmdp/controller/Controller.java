package com.hmdp.controller;

import com.hmdp.pojo.Employee;
import com.hmdp.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
@RestController
@RequestMapping("mybatis")
public class Controller {
    private static final Logger log = LoggerFactory.getLogger (Controller.class);
    @Autowired
    private Service service;
    
    @GetMapping
    public List<Employee> getEmployees() {
        List<Employee> employees = service.findAll ();
        log.info ("员工数据：{}", employees);
        return employees;
    }
}
