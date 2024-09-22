package com.fengxin.service;

import com.fengxin.dto.Result;
import com.fengxin.entity.ShopType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
public interface IShopTypeService extends IService<ShopType> {
    
    Result queryTypeList ();
}
