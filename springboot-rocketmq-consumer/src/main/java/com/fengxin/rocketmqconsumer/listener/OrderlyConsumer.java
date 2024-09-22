package com.fengxin.rocketmqconsumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.fengxin.rocketmqconsumer.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author FENGXIN
 * @date 2024/9/22
 * @project springboot-part
 * @description
 **/
@Slf4j
@Component
@RocketMQMessageListener (
        topic = "bootOrderlyTopic"
        , consumerGroup = "boot-orderly-consumer-group"
        // 设置单线程模式
        , consumeMode = ConsumeMode.ORDERLY
)
public class OrderlyConsumer implements RocketMQListener<MessageExt> {
    /**
     * 顺序消费
     * @param messageExt 消息体
     */
    @Override
    public void onMessage (MessageExt messageExt) {
        Order order = JSONObject.parseObject (new String (messageExt.getBody ()) , Order.class);
        System.out.println (order);
    }
}
