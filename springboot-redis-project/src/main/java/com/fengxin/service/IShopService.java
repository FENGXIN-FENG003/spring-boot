package com.fengxin.service;

import com.fengxin.dto.Result;
import com.fengxin.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
public interface IShopService extends IService<Shop> {
    
    Result queryShopById (Long id);
    
    Result updateShop (Shop shop);
    
    Result queryShopByType (Integer typeId , Integer current , Double x , Double y);
}
