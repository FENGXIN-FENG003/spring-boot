package com.hmdp.dto;

import lombok.Data;

import java.util.List;

/**
 * @author FENGXIN
 */
@Data
public class ScrollResult {
    private List<?> list;
    private Long minTime;
    private Integer offset;
}
