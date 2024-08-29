package com.fengxin.controller;

import com.fengxin.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
@RequestMapping("/d")
public class Controller {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @GetMapping("get")
    public List<Employee> hello() {
        return jdbcTemplate.query("select * from t_emp", new BeanPropertyRowMapper<> (Employee.class));
    }
    
}
