package com.fengxin.rocketmqconsumer.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author FENGXIN
 * @date 2024/9/22
 * @project springboot-part
 * @description
 **/
@Component
@RocketMQMessageListener (
        topic = "bootKeyTopic",
        consumerGroup = "boot-key-consumer-group"
)
public class KeyConsumer implements RocketMQListener<MessageExt> {
    /**
     * key
     * @param s 消息体
     */
    @Override
    public void onMessage (MessageExt s) {
        // key在消息头里
        System.out.println (s.getKeys ());
        System.out.println (new String (s.getBody ()));
    }
}
