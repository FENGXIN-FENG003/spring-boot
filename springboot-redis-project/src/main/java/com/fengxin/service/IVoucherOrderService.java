package com.fengxin.service;

import com.fengxin.dto.Result;
import com.fengxin.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
public interface IVoucherOrderService extends IService<VoucherOrder> {
    
    Result secKillVoucher (Long voucherId);
    
    Result createVoucherOrder (Long voucherId);
}
