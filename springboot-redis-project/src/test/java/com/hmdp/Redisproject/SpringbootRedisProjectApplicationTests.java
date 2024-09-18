package com.hmdp.Redisproject;

import com.hmdp.entity.Shop;
import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.RedisGenerateId;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootTest
class SpringbootRedisProjectApplicationTests {
    @Resource
    private ShopServiceImpl shopService;
    @Resource
    private RedisGenerateId redisGenerateId;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void a(){}

    /**
     * 测试逻辑过期解决缓存击穿
     */
    @Test
    void saveLogicTimeData () throws InterruptedException {
        shopService.saveLogicData (1L,10L);
    }
    
    /**
     * 批量导入geo数据 商户
     */
    @Test
    public void saveData(){
        // 查询所有商户数据
        List<Shop> shopList = shopService.list ();
        // 分类
        Map<Long, List<Shop>> shopMap = shopList.stream ().collect (Collectors.groupingBy (Shop::getTypeId));
        // 分批写入redis geo
        shopMap.forEach ((typeId , shops) -> {
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<> (shops.size ());
            // 批量存入shop
            for (Shop shop : shops) {
                locations.add (new RedisGeoCommands.GeoLocation<> (shop.getId ().toString () , new Point (shop.getX () , shop.getY ())));
            }
            stringRedisTemplate.opsForGeo ().add (RedisConstants.SHOP_GEO_KEY + typeId , locations);
        });
        
    }

    /**
     * 测试全局唯一id
     */
    private ExecutorService executorService = Executors.newFixedThreadPool (500);
    @Test
    public void generateId() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch (300);
        Runnable task = () -> {
            for (int i = 0 ; i < 100 ; i++) {
                long id = redisGenerateId.generateId ("order");
                System.out.println ("id = " + id);
            }
            countDownLatch.countDown ();
        };
        long begin = System.currentTimeMillis ();
        for (int i = 0 ; i < 300 ; i++) {
            executorService.submit (task);
        }
        countDownLatch.await ();
        long end = System.currentTimeMillis ();
        System.out.println ("time: " + (end - begin));
    }
}
