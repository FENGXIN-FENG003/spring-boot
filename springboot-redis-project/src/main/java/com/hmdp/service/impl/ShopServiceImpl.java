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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
        // 缓存穿透
        // Shop shop = getDataThrough (id);
        
        // 缓存击穿
        // Shop shop = getDataPunchThrough (id);
    }
    
    /**
     * 缓存穿透：查询一个不存在的数据，由于缓存不命中，将去查询数据库，但数据库中也不存在，这将导致每次查询都会去查询数据库，造成缓存穿透。
     * @param id 商户id
     * @return Shop
     */
    public Shop getDataThrough(Long id){
        String shopId = RedisConstants.CACHE_SHOP_KEY + id;
        // 1 先在redis查找缓存
        String shopData = stringRedisTemplate.opsForValue ().get (shopId);
        // 2 存在字符串 返回数据
        if (StringUtils.hasText (shopData)) {
            // 转换为对象
            return JSONObject.parseObject (shopData,Shop.class);
        }
        // 存在字符串 但是id为“”（redis存储的空数据为""）
        if (Objects.equals (shopData , "")){
            // 直接返回错误信息
            return null;
        }
        // 3 不存在
        // 3.1 查询数据库
        Shop shopById = getById (id);
        // 3.2 不存在 返回错误信息
        if (shopById == null) {
            // 防止缓存穿透 将空值写入redis
            stringRedisTemplate.opsForValue ().set (shopId, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        // 3.3 存在 将数据存入redis 并设置有效时间 手动实现数据库和缓存的数据一致性
        stringRedisTemplate.opsForValue ().set (shopId,JSONObject.toJSONString (shopById),RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        // 3.4 返回数据
        return shopById;
    }
    
    /**
     * 缓存击穿：缓存中没有但数据库中有的数据（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存没读到数据，又同时去数据库去取，引起数据库压力瞬间增大，造成过大压力
     */
    public Shop getDataPunchThrough(Long id){
        String shopId = RedisConstants.CACHE_SHOP_KEY + id;
        // 1 先在redis查找缓存
        String shopData = stringRedisTemplate.opsForValue ().get (shopId);
        // 2 存在字符串 返回数据
        if (StringUtils.hasText (shopData)) {
            // 转换为对象
            return JSONObject.parseObject (shopData,Shop.class);
        }
        // 存在字符串 但是id为“”（redis存储的空数据为""）
        if (Objects.equals (shopData , "")){
            // 直接返回错误信息
            return null;
        }
        // 3 不存在
        // 3.1 查询数据库
        Shop shopById = getById (id);
        // 3.2 不存在 返回错误信息
        if (shopById == null) {
            // 防止缓存穿透 将空值写入redis
            stringRedisTemplate.opsForValue ().set (shopId, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        // 3.3 存在 将数据存入redis 并设置有效时间 手动实现数据库和缓存的数据一致性
        stringRedisTemplate.opsForValue ().set (shopId,JSONObject.toJSONString (shopById),RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        // 3.4 返回数据
        return shopById;
    }
    /*
    保证缓存和数据库数据一致性 主动更新 先操作数据库 再删除缓存 并设置缓存超时及时更新缓存数据
    缓存命中则直接返回数据
    未命中则查询数据库 并更新缓存
    设置缓存超时：一种特殊情况 ：缓存过期了 在查询缓存时查询数据库的数据 在更新缓存之前有其他线程先更新了数据库的数据 此时缓存的数据和数据库的数据不一致
     */
    @Override
    // 添加事务 回滚错误
    @Transactional
    public Result updateShop (Shop shop) {
        if (shop.getId () == null) {
            return Result.fail ("商户id为空");
        }
        // 操作数据库
        updateById (shop);
        // 删除缓存
        stringRedisTemplate.delete (RedisConstants.CACHE_SHOP_KEY + shop.getId ());
        return Result.ok ();
    }
}
