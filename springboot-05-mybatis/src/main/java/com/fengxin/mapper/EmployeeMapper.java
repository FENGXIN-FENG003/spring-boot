package com.fengxin.mapper;

import com.fengxin.pojo.Employee;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
public interface EmployeeMapper {
    List<Employee> getEmployeeAll();
}
