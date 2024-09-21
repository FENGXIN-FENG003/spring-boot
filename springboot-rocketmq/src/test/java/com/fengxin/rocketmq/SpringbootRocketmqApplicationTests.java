package com.fengxin.rocketmq;

import com.fengxin.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
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
		// 发送 同步消息 需要等待mq主机和从机返回结果 再进行后续发送
		SendResult send = producer.send (message);
		System.out.println ("send = " + send.getSendStatus ());
		// 关闭生产者
		producer.shutdown ();
	}
	
	/**
	 * 异步消息
	 */
	@Test
	public void AAsyncProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("test_async_producer_group");
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		producer.start ();
		Message message = new Message ("testTopic","Hello asyncRocketmq".getBytes());
		producer.send (message,new SendCallback () {
			
			@Override
			public void onSuccess (SendResult sendResult) {
				log.info ("success");
			}
			
			@Override
			public void onException (Throwable throwable) {
				log.error (throwable.getMessage ());
			}
		});
		// 因为是异步的 所以需要等待异步执行结束
		log.info ("主线程");
		System.in.read ();
	}
	
	/**
	 * 单向消息
	 * 可用于不重要的消息 如日志处理 信息可能丢失 日志处理：像这样单独处理 保存信息到es
	 */
	@Test
	public void AOnewayProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer("test_oneway_producer_group");
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		producer.start ();
		Message message = new Message ("testTopic","Hello one way Rocketmq".getBytes());
		producer.sendOneway (message);
		log.info ("success");
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
