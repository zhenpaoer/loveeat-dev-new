package com.zz.order.service.impl;/**
 * Created by zhangzhen on 2020/12/17
 */

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.order.LeOrder;
import com.zz.framework.domain.order.LeOrderOperateLog;
import com.zz.order.config.RabbitMQConfig;
import com.zz.order.dao.LeOrderMapper;
import com.zz.order.dao.LeOrderOperateLogMapper;
import com.zz.order.feign.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName OrderMsgConsumer 订单消息消费者
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/12/17 23:18
 * @Version V1.0
 **/
@Slf4j
@Service
public class OrderMsgConsumer {
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	LeOrderMapper leOrderMapper;
	@Autowired
	BusinessService businessService;
	@Autowired
	LeOrderOperateLogMapper operateLogMapper;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	//注意此时监听的是死信队列
	@RabbitListener(queues= RabbitMQConfig.DEAD_QUEUEU)
	@RabbitHandler
	public void process(int oid) {
		log.info("接收到延时消息：oid="+oid);
		LeOrder order = leOrderMapper.getByOrderId(oid);
		if (order != null){
			int status = order.getStatus();
			log.info("status="+status);
			if (status == 0){
				String key = "product_" + order.getPid();
				Long expire = null;
				try {
					expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
				}
				log.info("expire="+expire);
				if (expire != null && expire <= 0){
					int closeOrderResult = leOrderMapper.closeOrder(order.getId());
					if (closeOrderResult>0){
						//修改商品状态为销售中
						ResponseResult responseResult = businessService.updateProductIsSaleToOne(order.getPid());
						if (responseResult.getCode() == 10000){
							//记录日志
							String logContent = "uid=" + order.getUid() + "用户未在规定时间内支付，已关闭订单";
							log.info(logContent);
							LeOrderOperateLog log = LeOrderOperateLog.builder().oid(oid)
									.content(logContent)
									.uid(order.getUid())
									.createTime(LocalDateTime.now())
									.updateTime(LocalDateTime.now())
									.build();
							operateLogMapper.insertOrderLog(log);
						}
					}else {
						log.info("关单失败");
					}
				}else {
					log.info("延迟队列有问题");
				}

			}
		}
	}
}
