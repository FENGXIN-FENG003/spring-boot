package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Override
    public Result queryShopById (Long id) {
        String shopId = RedisConstants.CACHE_SHOP_KEY + id;
        // 1 先在redis查找缓存
        String shopData = stringRedisTemplate.opsForValue ().get (shopId);
        // 2 存在 返回数据
        if (StringUtils.hasText (shopData)) {
            // 转换为对象
            Shop shop = JSONObject.parseObject (shopData,Shop.class);
            return Result.ok (shop);
        }
        // 3 不存在
        // 3.1 查询数据库
        Shop shopById = getById (id);
        // 3.2 不存在 返回错误信息
        if (shopById == null) {
            return Result.fail ("没有商户信息");
        }
        // 3.3 存在 将数据存入redis
        stringRedisTemplate.opsForValue ().set (shopId,JSONObject.toJSONString (shopById));
        // 3.4 返回数据
        return Result.ok (shopById);
    }
}
