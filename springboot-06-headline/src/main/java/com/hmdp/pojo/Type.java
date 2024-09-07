package com.hmdp.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * @author FENGXIN
 * @TableName news_type
 */
@TableName(value ="news_type")
@Data
public class Type implements Serializable {
    
    @TableId
    private Integer tid;

    private String tname;
    
    @Version
    private Integer version;
    
    @TableLogic
    private Integer isDeleted;

    @Serial
    private static final long serialVersionUID = 1L;
}