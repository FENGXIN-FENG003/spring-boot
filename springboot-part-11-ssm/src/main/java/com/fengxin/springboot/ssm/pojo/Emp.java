package com.fengxin.springboot.ssm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName t_emp
 */
@TableName(value ="t_emp")
@Data
public class Emp implements Serializable {
    private Integer empId;

    private String empName;

    private Double empSalary;

    private static final long serialVersionUID = 1L;
}