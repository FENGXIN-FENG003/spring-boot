package com.hmdp.springboot.ssm.mapper;

import com.hmdp.springboot.ssm.pojo.Emp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author FENGXIN
* @description 针对表【t_emp】的数据库操作Mapper
* @createDate 2024-08-29 11:52:40
* @Entity com.hmdp.springboot.ssm.pojo.Emp
*/
public interface EmpMapper extends BaseMapper<Emp> {
    Emp getByEmpIdBefore(@Param ("id") Long id);
}




