package com.hmdp.pojo;

import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/8/13
 * @project springboot-part
 * @description 接收前端请求分页数据
 **/
@Data
public class PortalVo {
    private String keyWords;
    private Integer type;
    private Integer pageNum = 1;
    private Integer pageSize =10;
}
