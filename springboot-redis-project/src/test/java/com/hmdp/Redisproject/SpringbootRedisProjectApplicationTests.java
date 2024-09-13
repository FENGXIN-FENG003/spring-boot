package com.hmdp.Redisproject;

import com.hmdp.service.impl.ShopServiceImpl;
import com.hmdp.utils.RedisGenerateId;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
