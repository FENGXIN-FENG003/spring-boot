package com.hmdp.springboot.redis;

import com.alibaba.fastjson2.JSONObject;
import com.hmdp.springboot.redis.pojo.Employee;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author FENGXIN
 * @date 2024/9/5
 * @project springboot-part
 * @description key和value都使用StringRedisTemplate 不再使用RedisTemplate（对象时会存储类class 占用空间）
 **/
@SpringBootTest
public class RedisStringTempTests {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    @Test
    public void testString(){
        // 准备对象
        Employee employee = new Employee ("枫",18);
        // 手动序列化
        String jsonString = JSONObject.toJSONString (employee);
        // 存入redis
        stringRedisTemplate.opsForValue ().set ("Emp",jsonString);
        // 读取数据
        String emp = stringRedisTemplate.opsForValue ().get ("Emp");
        JSONObject jsonObject = JSONObject.parseObject (emp);
        System.out.println ("jsonObject = " + jsonObject);
    }
}
