package com.fengxin.springboot.springsecurity.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class User implements Serializable {
    @TableId
    private Long id;

    private String userName;

    private String nickName;

    private String password;

    private String status;

    private String email;

    private String phonenumber;

    private String sex;

    private String avatar;

    private String userType;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private Integer delFlag;

    @Serial
    private static final long serialVersionUID = 1L;
}