package com.fengxin.rocketmqconsumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
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
        topic = "bootTagTopic"
        ,consumerGroup = "boot-tag-consumer-group"
        // 过滤tag
        ,selectorType = SelectorType.TAG
        ,selectorExpression = "tag1 || tag2"
)
public class TagConsumer implements RocketMQListener<MessageExt> {
    /**
     * tag参数
     * @param s 消息体
     */
    @Override
    public void onMessage (MessageExt s) {
        log.info (new String (s.getBody ()));
    }
}
