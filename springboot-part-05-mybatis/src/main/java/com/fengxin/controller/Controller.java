package com.fengxin.controller;

import com.fengxin.mapper.EmployeeMapper;
import com.fengxin.pojo.Employee;
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
    private EmployeeMapper employeeMapper;
    
    @GetMapping
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeMapper.getEmployeeAll ();
        log.info ("员工数据：{}", employees);
        return employees;
    }
}
