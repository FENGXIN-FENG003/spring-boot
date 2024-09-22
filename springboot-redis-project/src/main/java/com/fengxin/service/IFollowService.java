package com.fengxin.service;

import com.fengxin.dto.Result;
import com.fengxin.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
public interface IFollowService extends IService<Follow> {
    
    Result follow (Long id , Boolean isFollow);
    
    Result isFollow (Long id);
    
    Result common (Long id);
}
