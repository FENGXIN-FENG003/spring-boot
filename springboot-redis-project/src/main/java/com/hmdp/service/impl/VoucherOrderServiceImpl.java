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
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 1.加悲观🔒 实现一人一单
        // 一人一单即对象 高并发环境下 如果🔒方法 每个用户就是串行化购买 因此需要🔒具体用户 即🔒用户id
        // 从拦截器存入的数据获取user_id
        Long userId = UserHolder.getUser ().getId ();
        // 2.使用intern 从常量池返回同一个字符串 确保是相同唯一对象 而不是同一个用户不同的对象 这样会导致上锁逻辑错误
        synchronized (userId.toString ().intern ()){
            // 3.这里是this调用 不会生效事务 需要代理对象操作 即从spring中获取代理调用此方法 实现事务
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy ();
            return proxy.createVoucherOrder (voucherId);
        }
       
    }
    
    @Transactional
    @Override
    public Result createVoucherOrder(Long voucherId){
        // 4.先判断是否已经抢购过优惠券
        LambdaQueryWrapper<VoucherOrder> queryWrapper = new LambdaQueryWrapper<> ();
        // 从拦截器存入的数据获取user_id
        Long userId = UserHolder.getUser ().getId ();
        queryWrapper.eq (VoucherOrder::getUserId,userId)
                .eq (VoucherOrder::getVoucherId,voucherId);
        if (getOne (queryWrapper) != null){
            return Result.fail ("您已经拥有该优惠券！");
        }
        // 5.扣减库存
        LambdaUpdateWrapper<SeckillVoucher> updateWrapper = new LambdaUpdateWrapper<> ();
        updateWrapper
                .setSql ("stock = stock - 1")
                // 5.1设置乐观锁
                .gt (SeckillVoucher::getStock,0)
                .eq (SeckillVoucher::getVoucherId,voucherId);
        boolean update = voucherOrderService.update (updateWrapper);
        if (!update) {
            return Result.fail ("抢购失败");
        }
        // 6.设置订单信息
        VoucherOrder voucherOrder = new VoucherOrder ();
        voucherOrder.setVoucherId (voucherId);
        long generateId = redisGenerateId.generateId ("order");
        voucherOrder.setId (generateId);
        voucherOrder.setUserId (userId);
        // 7.存入数据库
        save (voucherOrder);
        // 返回
        return Result.ok (generateId);
    }
}
