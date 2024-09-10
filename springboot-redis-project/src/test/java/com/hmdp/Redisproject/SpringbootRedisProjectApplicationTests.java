package com.hmdp.Redisproject;

import com.hmdp.service.impl.ShopServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRedisProjectApplicationTests {
    @Resource
    private ShopServiceImpl shopService;
    
    @Test
    void a(){}
    @Test
    void saveLogicTimeData () throws InterruptedException {
        shopService.saveLogicData (1L,10L);
    }
    
}
