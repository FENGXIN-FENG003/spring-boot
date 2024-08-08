package com.fengxin.service;

import com.fengxin.mapper.EmployeeMapper;
import com.fengxin.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private EmployeeMapper employeeMapper;
    
    // 使用事务注解 导入spring-boot-starter-jdbc即可加注解使用事务
    @Transactional
    public List<Employee> findAll() {
        return employeeMapper.getEmployeeAll ();
    }
}
