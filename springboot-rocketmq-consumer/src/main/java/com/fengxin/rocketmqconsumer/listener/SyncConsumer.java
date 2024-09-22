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
@RocketMQMessageListener (topic = "bootSyncTopic", consumerGroup = "boot-consumer-group")
public class SyncConsumer implements RocketMQListener<MessageExt> {
    /**
     * 同步消费
     * @param messageExt 消息体
     */
    @Override
    public void onMessage (MessageExt messageExt) {
        System.out.println (new String(messageExt.getBody()));
    }
}
