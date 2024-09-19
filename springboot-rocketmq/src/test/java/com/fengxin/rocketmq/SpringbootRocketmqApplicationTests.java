package com.fengxin.rocketmq;

import com.fengxin.constant.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringbootRocketmqApplicationTests {
	
	/**
	 * 消息发送
	 */
	@Test
	void AProducer() throws Exception {
		// 创建生产者
		DefaultMQProducer producer = new DefaultMQProducer("test_producer_group");
		// 连接
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		// 启动
		producer.start ();
		// 设置消息
		Message message = new Message ("testTopic","Hello Rocketmq".getBytes());
		// 发送
		SendResult send = producer.send (message);
		System.out.println ("send = " + send.getSendStatus ());
		// 关闭生产者
		producer.shutdown ();
	}
	
	/**
	 * 消息消费
	 */
	@Test
	void AConsumer() throws Exception {
		// 创建消费
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_consumer_group");
		// 连接
		consumer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		// 消息订阅 * 代表订阅所有消息 后期会做消息过滤
		consumer.subscribe ("testTopic", "*");
		// 消息处理 设置监听器 一直监听 异步回调
		consumer.registerMessageListener (new MessageListenerConcurrently () {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage (List<MessageExt> list , ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				System.out.println ("我是消费者");
				list.forEach (message -> {
					System.out.println (list);
					System.out.println (new String (message.getBody()));
				});
				System.out.println ("消费者上下文：" + consumeConcurrentlyContext);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		// 启动
		consumer.start();
		// 设置一直监听 异步 不能线程结束就不监听
		System.in.read ();
	}

}
