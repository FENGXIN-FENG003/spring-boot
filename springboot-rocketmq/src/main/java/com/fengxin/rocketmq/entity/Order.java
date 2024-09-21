package com.fengxin.rocketmq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author FENGXIN
 * @date 2024/9/21
 * @project springboot-part
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderName;
    private Integer userId;
    private String detail;
}
