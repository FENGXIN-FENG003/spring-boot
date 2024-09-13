package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisGenerateId;
import com.hmdp.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    @Resource
    private ISeckillVoucherService voucherOrderService;
    @Resource
    private RedisGenerateId redisGenerateId;
    
    
    /**
     * 实现秒杀优惠券下单功能
     * @param voucherId 优惠券id
     * @return 结果状态
     */
    @Override
    public Result secKillVoucher (Long voucherId) {
        // 查询优惠券
        SeckillVoucher seckillVoucher = voucherOrderService.getById (voucherId);
        // 是否开始 && 是否结束
        if (seckillVoucher.getBeginTime ().isAfter (LocalDateTime.now ()) || seckillVoucher.getEndTime ().isBefore (LocalDateTime.now ())) {
            return Result.fail ("优惠券不在抢购时间内！");
        }
        // 库存是否足够
        if (seckillVoucher.getStock () < 1) {
            return Result.fail ("优惠券已经抢光！");
        }
        // 扣减库存
        seckillVoucher.setStock (seckillVoucher.getStock () - 1);
        LambdaQueryWrapper<SeckillVoucher> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
        lambdaQueryWrapper.eq (SeckillVoucher::getVoucherId,voucherId);
        voucherOrderService.update (seckillVoucher,lambdaQueryWrapper);
        // 设置订单信息
        VoucherOrder voucherOrder = new VoucherOrder ();
        voucherOrder.setVoucherId (voucherId);
        long generateId = redisGenerateId.generateId ("order");
        voucherOrder.setId (generateId);
        // 从拦截器存入的数据获取user_id
        voucherOrder.setUserId (UserHolder.getUser ().getId ());
        // 存入数据库
        save (voucherOrder);
        // 返回
        return Result.ok (generateId);
    }
}
