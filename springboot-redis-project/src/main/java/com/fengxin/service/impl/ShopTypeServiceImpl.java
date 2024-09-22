package com.fengxin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fengxin.dto.Result;
import com.fengxin.entity.ShopType;
import com.fengxin.mapper.ShopTypeMapper;
import com.fengxin.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxin.utils.RedisConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Override
    public Result queryTypeList () {
        String shopType = RedisConstants.CACHE_TYPE_KEY;
        // 1 在redis查询type
        Set<String> type = stringRedisTemplate.opsForZSet ().range (shopType , 0 , 9);
        // 转换为list
        List<ShopType> shopTypes = new ArrayList<> ();
        if (type != null && !type.isEmpty ()) {
            for (String s : type) {
                shopTypes.add (JSONObject.parseObject (s, ShopType.class));
            }
            // 2 存在 返回数据
            return Result.ok (shopTypes);
        }
        // 3 查询数据库
        // 获取的是ShopType类对象的List
        List<ShopType> typeList = query ().orderByAsc ("sort").list ();
        // 3.1 不存在 返回错误信息
        if (typeList == null || typeList.isEmpty ()) {
            return Result.fail ("商品类型不存在");
        }
        // 3.2 存在
        // 3.3 存入redis
        for (ShopType shopType1 : typeList) {
            // 存储为zset
            stringRedisTemplate.opsForZSet ().add (shopType, JSONObject.toJSONString (shopType1),shopType1.getSort ());
        }
        Set<String> type1 = stringRedisTemplate.opsForZSet ().range (shopType , 0 , 9);
        // 转换为list
        if (type1 != null) {
            for (String s : type1) {
                shopTypes.add (JSONObject.parseObject (s, ShopType.class));
            }
        }
        // 3.4 返回数据
        return Result.ok (shopTypes);
    }
}
