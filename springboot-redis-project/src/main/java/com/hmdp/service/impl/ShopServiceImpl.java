package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RedisData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
@Service
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Override
    public Result queryShopById (Long id) {
        // 缓存穿透
        // Shop shop = getDataThrough (id);
        
        // 基于互斥锁解决缓存击穿
        Shop shop = logicSolvePunchThrough (id);
        if (shop == null) {
            return Result.fail ("商户信息错误");
        }
        return Result.ok (shop);
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
            // 转换为对象并返回
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
     * 基于互斥锁解决缓存击穿：缓存中没有但数据库中有的数据（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存没读到数据，又同时去数据库去取，引起数据库压力瞬间增大，造成过大压力
     */
    public Shop lockSolvePunchThrough(Long id){
        Shop shopById;
        try {
            String shopId = RedisConstants.CACHE_SHOP_KEY + id;
            // 1 先在redis查找缓存
            String shopData = stringRedisTemplate.opsForValue ().get (shopId);
            // 2 存在字符串 返回数据
            if (StringUtils.hasText (shopData)) {
                // 转换为对象并返回
                return JSONObject.parseObject (shopData,Shop.class);
            }
            // 存在字符串 但是id为“”（redis存储的空数据为""）
            if (Objects.equals (shopData , "")){
                // 直接返回错误信息
                return null;
            }
            // 3 不存在
            // 4 缓存击穿
            // 4.1 获取🔒
            boolean tryLock = tryLock (id);
            // 4.2 失败 休眠一会儿 重新尝试
            if (!tryLock) {
                Thread.sleep (10);
                // 重试
                lockSolvePunchThrough (id);
            }
            // 4.3 成功
            // 成功获取锁 有可能缓存已经重建 因此做二次判断
            // 先在redis查找缓存
            shopData = stringRedisTemplate.opsForValue ().get (shopId);
            // 存在字符串 返回数据
            if (StringUtils.hasText (shopData)) {
                // 转换为对象并返回
                return JSONObject.parseObject (shopData,Shop.class);
            }
            // 存在字符串 但是id为“”（redis存储的空数据为""）
            if (Objects.equals (shopData , "")){
                // 直接返回错误信息
                return null;
            }
            // 5 查询数据库
            shopById = getById (id);
            // 模拟重建延时
            Thread.sleep (200);
            // 6 不存在 返回错误信息
            if (shopById == null) {
                // 防止缓存穿透 将空值写入redis
                stringRedisTemplate.opsForValue ().set (shopId, "", RedisConstants.CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            // 7 存在 将数据存入redis 并设置有效时间 手动实现数据库和缓存的数据一致性
            stringRedisTemplate.opsForValue ().set (shopId,JSONObject.toJSONString (shopById),RedisConstants.CACHE_SHOP_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException (e);
        } finally {
            // 8 不论是否更新成功 都释放🔒 防止死🔒
            delLock (id);
        }
        // 8 返回数据
        return shopById;
    }
    
    // 线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool (10);
    /**
     * 基于逻辑过期解决缓存击穿
     * @param id 商户id
     * @return Shop
     */
    public Shop logicSolvePunchThrough(Long id){
        String shopId = RedisConstants.CACHE_SHOP_KEY + id;
        // 1 先在redis查找缓存
        String shopData = stringRedisTemplate.opsForValue ().get (shopId);
        // 2 不存在
        // 因为已经缓存预热商品 逻辑过期 所以商品会一直存在缓存 不会消失 除非手动删除
        // 因此不存在就是商品不在活动里 直接返回null
        if (!StringUtils.hasText (shopData)) {
            return null;
        }
        
        // 存在 转换为对象
        RedisData redisData = JSONObject.parseObject (shopData , RedisData.class);
        // 获取商品
        JSONObject data = (JSONObject) redisData.getData ();
        Shop shop = JSONUtil.toBean (data.toJSONString () , Shop.class);
        // 是否过期
        // 未过期
        if(redisData.getExpireTime ().isAfter (LocalDateTime.now ())){
            return shop;
        }
        // 过期
        // 获取🔒
        if (tryLock (id)) {
            // 获取成功
            // 二次判断 是否过期 如果已经有线程更新了缓存则直接返回数据
            if (redisData.getExpireTime ().isAfter (LocalDateTime.now ())) {
                return shop;
            }
            // 开启新线程重建缓存
            CACHE_REBUILD_EXECUTOR.submit (() -> {
                try {
                    this.saveLogicData (id , 20L);
                } catch (Exception e) {
                    throw new RuntimeException (e);
                } finally {
                    // 释放锁
                    delLock (id);
                }
            });
        }
        // 返回旧缓存 不再等待
        return shop;
    }
    
    /**
     * 获取🔒
     * @param id 商户id
     * @return boolean
     */
    public boolean tryLock(Long id){
        // 获取🔒
        // 返回🔒设置成功的标记 存在了就获取失败
        Boolean flag = stringRedisTemplate.opsForValue ().setIfAbsent (RedisConstants.LOCK_SHOP_KEY + id , "1" , RedisConstants.LOCK_SHOP_TTL , TimeUnit.SECONDS);
        return BooleanUtil.isTrue (flag);
    }
    
    /**
     * 释放🔒
     * @param id 商户id
     */
    public void delLock(Long id){
        // 释放🔒
        // 数据库查询数据成功 缓存更新成功 释放🔒
        Boolean flag = stringRedisTemplate.delete (RedisConstants.LOCK_SHOP_KEY + id);
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
    
    /**
     * 封装逻辑过期时间
     * 数据会一直存在缓存 是否删除需要手动逻辑判断 用于活动限时的商品过期
     * @param id 商户id
     */
    public void saveLogicData(Long id,Long expireTime) throws InterruptedException {
        // 查询数据库商品
        Shop byId = getById (id);
        // 模拟耗时
        Thread.sleep (200);
        // 封装逻辑过期
        RedisData redisData = new RedisData ();
        redisData.setData (byId);
        redisData.setExpireTime (LocalDateTime.now ().plusSeconds (expireTime));
        // 存入redis缓存
        stringRedisTemplate.opsForValue ().set (RedisConstants.CACHE_SHOP_KEY + id,JSONObject.toJSONString (redisData));
        log.info ("存入redis数据成功:" + byId.getName ());
    }
}
