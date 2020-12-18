package com.zz.order.config;/**
 * Created by zhangzhen on 2020/12/17
 */

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RabbitMQConfig
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/12/17 18:56
 * @Version V1.0
 **/
@Configuration
public class RabbitMQConfig {
	//正常 交换器、队列、key
	public static final String ORDINARY_EXCHANGE = "ordinary_exchange";
	public static final String ORDINARY_ROUTEKEY = "ordinary_routekey";
	public static final String ORDINARY_QUEUE = "ordinary_queue";

	// 死信 交换器、队列、key
	public static final String DEAD_EXCHANGE = "dead_exchange";
	public static final String DEAD_ROUTEKEY = "dead_routekey";
	public static final String DEAD_QUEUEU = "dead_queue";


	@Bean
	public DirectExchange ordinaryExchange() {
		return new DirectExchange(ORDINARY_EXCHANGE, true, false);
	}
	@Bean
	public Queue ordinaryQueue() {
		Map<String, Object> map = new HashMap<>();
		//把死信 交换器和路由键  绑定到普通队列中
		map.put("x-dead-letter-exchange", DEAD_EXCHANGE);
		map.put("x-dead-letter-routing-key", DEAD_ROUTEKEY);
		Queue queue = new Queue(ORDINARY_QUEUE, true, false, false, map);
		return queue;
	}
	//把普通 交换器、队列、路由键  绑定在一块
	@Bean
	public Binding queueDeadBinding() {
		return BindingBuilder.bind(ordinaryQueue()).to(ordinaryExchange()).with(ORDINARY_ROUTEKEY);
	}





	//把死信 交换器、队列、路由键  绑定在一块
	@Bean
	public DirectExchange deadExchange() {
		return new DirectExchange(DEAD_EXCHANGE, true, false);
	}
	@Bean
	public Queue deadQueue() {
		return new Queue(DEAD_QUEUEU, true, false, false);
	}


	@Bean
	public Binding queueTransBinding() {
		return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTEKEY);
	}
}
