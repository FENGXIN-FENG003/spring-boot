package com.fengxin.springboot.ssm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxin.springboot.ssm.pojo.Emp;
import com.fengxin.springboot.ssm.service.EmpService;
import com.fengxin.springboot.ssm.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author FENGXIN
* @description 针对表【t_emp】的数据库操作Service实现
* @createDate 2024-08-29 11:52:40
*/
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp>
    implements EmpService{
    @Autowired
    EmpMapper empMapper;
    
    public Emp queryById(Long id){
        return empMapper.getByEmpIdBefore (id);
    }
    
}




