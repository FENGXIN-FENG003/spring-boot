package com.fengxin.rocketmqconsumer.listener;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
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
    
    @Override
    public void onMessage (MessageExt s) {
        System.out.println (s.getKeys ());
        System.out.println (new String (s.getBody ()));
    }
}
