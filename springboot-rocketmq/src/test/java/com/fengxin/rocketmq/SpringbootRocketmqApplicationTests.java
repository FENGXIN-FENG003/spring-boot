package com.fengxin.rocketmq;

import com.fengxin.rocketmq.constant.MqConstant;
import com.fengxin.rocketmq.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
		Message message = new Message ("asyncTopic","Hello asyncRocketmq".getBytes());
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
		Message message = new Message ("onewayTopic","Hello one way Rocketmq".getBytes());
		producer.sendOneway (message);
		log.info ("success");
		producer.shutdown ();
	}
	
	/**
	 * 延时消息
	 */
	@Test
	public void AMsProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer ("test_ms_producer_group");
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		producer.start ();
		Message message = new Message ("msTopic","Hello ms Rocketmq".getBytes());
		// 设置延时时间
		message.setDelayTimeLevel (3);
		producer.send (message);
		log.info ("success" + new Date ());
		producer.shutdown ();
	}
	
	/**
	 * 批量消息发送
	 */
	@Test
	public void ABatchProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer ("test_batch_producer_group");
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		producer.start ();
		List <Message> messages = Arrays.asList (
				new Message ("batchTopic","Hello batch Rocketmq111".getBytes()),
				new Message ("batchTopic","Hello batch Rocketmq222".getBytes()),
				new Message ("batchTopic","Hello batch Rocketmq333".getBytes())
		);
		producer.send (messages);
		log.info ("success");
		producer.shutdown ();
	}
	
	/**
	 * 顺序消息
	 * 1. producer:保证需要顺序消费的消息在同一队列里 而不是轮询
	 * 2. consumer:保证消费者单线程接收顺序消息 不必全局顺序 保证需要顺序消费的消息即可
	 */
	@Test
	public void AOrderlyProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer ("test_orderly_producer_group");
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		producer.start ();
		List<Order> orders = Arrays.asList (
				new Order ("aaa",1,"下单"),
				new Order ("aaa",1,"消息"),
				new Order ("aaa",1,"物流"),
				new Order ("bbb",2,"下单"),
				new Order ("bbb",2,"消息"),
				new Order ("bbb",2,"物流")
		);
		// 发送
		orders.forEach (order -> {
			Message message = new Message ("orderTopic",order.toString ().getBytes());
			// 设置顺序发送
            try {
                producer.send (message , new MessageQueueSelector () {
                    @Override
                    public MessageQueue select (List<MessageQueue> list , Message message , Object o) {
	                    int hashCode = o.hashCode ();
						// 设置固定的队列
						hashCode %= list.size ();
						// 存入队列
	                    return list.get (hashCode);
                    }
                },order.getOrderName ());
            } catch (Exception e) {
                throw new RuntimeException (e);
            }
        });
		log.info ("success");
		producer.shutdown ();
	}
	
	/**
	 * tag参数 对Topic更深层次划分
	 * 只有符合相应的tag订阅一致性才能接收消息 过滤消息
	 */
	@Test
	public void ATagProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer ("test_tag_producer_group");
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		producer.start ();
		Message message1 = new Message ("tagTopic","tag1","Hello tag Rocketmq111".getBytes());
		Message message2 = new Message ("tagTopic","tag2","Hello tag Rocketmq222".getBytes());
		producer.send (message1);
		producer.send (message2);
		log.info ("success");
		producer.shutdown ();
	}
	
	/**
	 * key 自定义唯一标识
	 */
	@Test
	public void AKeyProducer() throws Exception {
		DefaultMQProducer producer = new DefaultMQProducer ("test_key_producer_group");
		producer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		producer.start ();
		String key = String.valueOf (UUID.randomUUID ());
		Message message = new Message ("keyTopic","tag",key,"Hello key Rocketmq111".getBytes());
		producer.send (message);
		log.info ("success");
		producer.shutdown ();
	}
	
/*---------------------------------------------------------------------------------------------------------------/
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
	/**
	 * 延时消费
	 */
	@Test
	public void AMsConsumer() throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_ms_consumer_group");
		consumer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		consumer.subscribe ("msTopic", "*");
		consumer.registerMessageListener (new MessageListenerConcurrently () {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage (List<MessageExt> list , ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				log.info ("接受时间：" + new Date ());
				log.info (new String (list.get (0).getBody()));
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.in.read ();
	}
	/**
	 * 批量消费
	 */
	@Test
	public void ABatchConsumer() throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_batch_consumer_group");
		consumer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		consumer.subscribe ("batchTopic", "*");
		consumer.registerMessageListener (new MessageListenerConcurrently () {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage (List<MessageExt> list , ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				log.info (new String (list.get (0).getBody()));
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.in.read ();
	}
	
	/**
	 * 顺序消费
	 */
	@Test
	public void AOrderlyConsumer() throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_orderly_consumer_group");
		consumer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		consumer.subscribe ("orderTopic", "*");
		// 使用单线程消费
		consumer.registerMessageListener (new MessageListenerOrderly () {
			@Override
			public ConsumeOrderlyStatus consumeMessage (List<MessageExt> list , ConsumeOrderlyContext consumeOrderlyContext) {
				// 线程日志
				log.info (Thread.currentThread ().getName ());
				log.info (new String (list.get (0).getBody()));
				return ConsumeOrderlyStatus.SUCCESS;
			}
		});
		consumer.start();
		System.in.read ();
	}
	
	/**
	 * tag1消费
	 */
	@Test
	public void ATag1Consumer() throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_tag_consumer_group");
		consumer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		consumer.subscribe ("tagTopic", "tag1");
		consumer.registerMessageListener (new MessageListenerConcurrently () {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage (List<MessageExt> list , ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				log.info (new String (list.get (0).getBody()));
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.in.read ();
	}
	
	/**
	 * tag1 2消费
	 */
	@Test
	public void ATag12Consumer() throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_tag_consumer_group");
		consumer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		consumer.subscribe ("tagTopic", "tag1 || tag2");
		consumer.registerMessageListener (new MessageListenerConcurrently () {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage (List<MessageExt> list , ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				log.info (new String (list.get (0).getBody()));
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.in.read ();
	}
	
	/**
	 * key消费
	 */
	@Test
	public void AKeyConsumer() throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_key_consumer_group");
		consumer.setNamesrvAddr (MqConstant.NAMESRV_ADDR);
		consumer.subscribe ("keyTopic", "*");
		consumer.registerMessageListener (new MessageListenerConcurrently () {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage (List<MessageExt> list , ConsumeConcurrentlyContext consumeConcurrentlyContext) {
				log.info (new String (list.get (0).getBody()));
				// 获取key
				log.info (list.get (0).getKeys());
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		System.in.read ();
	}
	
}
