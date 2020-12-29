package com.zz.order.service.impl;

import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.framework.domain.business.response.ProductCode;
import com.zz.framework.domain.order.LeOrder;
import com.zz.framework.domain.order.LeOrderOperateLog;
import com.zz.framework.domain.order.response.OrderCode;
import com.zz.framework.utils.SnowflakeIdWorker;
import com.zz.order.config.RabbitMQConfig;
import com.zz.order.dao.LeOrderMapper;
import com.zz.order.dao.LeOrderOperateLogMapper;
import com.zz.order.feign.BusinessService;
import com.zz.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Many;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	LeOrderMapper leOrderMapper;
	@Autowired
	BusinessService businessService;
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Autowired
	LeOrderOperateLogMapper operateLogMapper;

	@Autowired
	private AmqpTemplate amqpTemplate;
	int orderSeconds = 300;


	@Transactional
	public ResponseResult createOrder(int uid, int pid, HttpServletRequest request){
		int id = pid;
		GetLeProductPicMenuExtResult productResult = businessService.getProductById(id);
		if (productResult.getCode() != 10000){
			ExceptionCast.cast(CommonCode.FAIL);
		}
		LeProduct leProduct = productResult.getLeProductPicMenuExt().getLeProduct();
		if (leProduct == null ){
			ExceptionCast.cast(ProductCode.PRODUCT_NOTCOMPLETE);
		}
		if (leProduct.getIssale() == 3){
			//今日已售
			ExceptionCast.cast(OrderCode.ORDER_SALED);
		}
		if (leProduct.getIssale() == 4){
			//支付中的
			//判断是不是自己的订单
			LeOrder byPidAndDate = leOrderMapper.getByPidAndDate(pid, LocalDate.now());
			if (byPidAndDate == null){
				ExceptionCast.cast(CommonCode.FAIL);
			}
			int uid1 = byPidAndDate.getUid();
			if (uid1 == uid) {
				ExceptionCast.cast(OrderCode.ORDER_REBUY);
			}else {
				ExceptionCast.cast(OrderCode.ORDER_PROCESSING);
			}
		}
		Long expire = null;
		try {
			//先查redis存不存在
			String key = "product_" + pid;
			String value = stringRedisTemplate.opsForValue().get(key);
			if (StringUtils.isBlank(value)){
				//不存在 则可以下单
				value = "uid = " + uid ;
				stringRedisTemplate.boundValueOps(key).set(value,orderSeconds, TimeUnit.SECONDS);
				//修改商品状态为销售中
//				ResponseResult responseResult = businessService.updateProductIsSaleByPid(pid, 4);
//				if (responseResult.getCode() == 10000){
					//下订单
					SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1,1);
					String tradeNo = snowflakeIdWorker.nextId()+"";
					LeOrder order = LeOrder.builder().bid(leProduct.getBid())
							.pid(pid)
							.tradeNo(tradeNo)
							.uid(uid)
							.price(leProduct.getBargainprice())
							.createDate(LocalDate.now())
							.createTime(LocalDateTime.now())
							.updateTime(LocalDateTime.now())
							.status(0) //创建
							.build();
					int insert = leOrderMapper.insertOrder(order);
					if (insert > 0){
						LeOrder localOrder = leOrderMapper.getOne(uid, pid, LocalDate.now());
						int oid = localOrder.getId();
						//记录日志
						String logContent = "uid=" + uid + "用户创建了订单["+oid+"],额度=" + localOrder.getPrice();
						log.info(logContent);
						LeOrderOperateLog log = LeOrderOperateLog.builder().oid(oid)
								.content(logContent)
								.uid(uid)
								.createTime(LocalDateTime.now())
								.updateTime(LocalDateTime.now())
								.build();
						operateLogMapper.insertOrderLog(log);

						sendDelayMsg(oid,orderSeconds);
						return  new ResponseResult(CommonCode.SUCCESS);
					}else {
						ExceptionCast.cast(CommonCode.FAIL);
					}
//				}
//				else {
//					ExceptionCast.cast(CommonCode.FAIL);
//				}
			}else {
				//存在
				ExceptionCast.cast(OrderCode.ORDER_PROCESSING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		return new ResponseResult(CommonCode.FAIL);
	}

	//延迟队列 发送延迟消息 5分钟没有支付就关闭订单
	private void sendDelayMsg(int oid,int time) {

		//rabbit默认为毫秒级
		long times = time * 1000;
		MessagePostProcessor processor = message -> {
			message.getMessageProperties().setExpiration(String.valueOf(times));
			return message;
		};
		amqpTemplate.convertAndSend(RabbitMQConfig.ORDINARY_EXCHANGE, RabbitMQConfig.ORDINARY_ROUTEKEY, oid, processor);
		log.info("我发送了消息，oid="+oid);
	}

	//查询用户的订单
	public ResponseResultWithData getByUid(int uid){
		List<LeOrder> byUid = leOrderMapper.getByUid(uid);
		if (byUid.size() == 0){
			return new ResponseResultWithData(CommonCode.SUCCESS,null);
		}else {
			HashMap<String,Object> map = new HashMap<>();
			map.put("data",byUid);
			return new ResponseResultWithData(CommonCode.SUCCESS,map);
		}
	}

	@Override
	//查询这个商品今天是否被这个用户下单
	public boolean isOrdered(int pid, int uid) {
		LeOrder order = leOrderMapper.getByPidAndUidAndDate(pid, uid, LocalDate.now());
		if (order != null){
			return true;
		}
		return false;
	}
}
