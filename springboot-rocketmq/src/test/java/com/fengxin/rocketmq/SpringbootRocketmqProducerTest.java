package com.fengxin.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.fengxin.rocketmq.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/9/22
 * @project springboot-part
 * @description
 **/
@Slf4j
@SpringBootTest
public class SpringbootRocketmqProducerTest {
    // 注入mq
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    /**
     * 同步消息
     */
    @Test
    public void syncSend(){
        rocketMQTemplate.syncSend("bootSyncTopic", "hello world");
    }
    
    /**
     * 异步消息
     */
    @Test
    public void asyncSend(){
        rocketMQTemplate.asyncSend ("bootAsyncTopic" , "hello async" , new SendCallback () {
            @Override
            public void onSuccess (SendResult sendResult) {
                log.info (sendResult.getMsgId());
            }
            
            @Override
            public void onException (Throwable e) {
                log.info (e.getMessage());
            }
        });
    }
    
    /**
     * 单向消息
     */
    @Test
    public void sendOneway(){
        rocketMQTemplate.sendOneWay ("bootOnewayTopic" , "hello world");
    }
    
    /**
     * 延时消息
     */
    @Test
    public void sendMs(){
        Message<String> msg = MessageBuilder.withPayload ("延时消息").build ();
        rocketMQTemplate.syncSend ("bootMsTopic" , msg,3000,2);
    }
    
    /**
     * 顺序消息
     */
    @Test
    public void sendOrderly(){
        List<Order> orders = Arrays.asList (
                new Order ("aaa",1,"下单"),
                new Order ("aaa",1,"消息"),
                new Order ("aaa",1,"物流"),
                new Order ("bbb",2,"下单"),
                new Order ("bbb",2,"消息"),
                new Order ("bbb",2,"物流")
        );
        orders.forEach (order -> {
            // 使用json发送对象消息
            rocketMQTemplate.syncSendOrderly ("bootOrderlyTopic" ,JSONObject.toJSONString (order),order.getOrderName ());
        });
    }
    
    /**
     * tag参数
     */
    @Test
    public void sendTag(){
        // topic带上tag即可
        rocketMQTemplate.syncSend ("bootTagTopic:tag1" , "tag参数");
    }
    
    /**
     * key
     */
    @Test
    public void sengKey(){
        // 设置key在消息头
        Message<String> message = MessageBuilder.withPayload ("key参数").setHeader (RocketMQHeaders.KEYS , "aaa,bbb,ccc").build ();
        rocketMQTemplate.syncSend ("bootKeyTopic" , message);
    }
}
