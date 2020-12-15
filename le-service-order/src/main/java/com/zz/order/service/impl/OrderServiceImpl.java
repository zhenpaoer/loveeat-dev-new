package com.zz.order.service.impl;

import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.framework.domain.business.response.ProductCode;
import com.zz.framework.domain.order.LeOrder;
import com.zz.framework.domain.order.LeOrderOperateLog;
import com.zz.framework.domain.order.response.OrderCode;
import com.zz.order.dao.LeOrderMapper;
import com.zz.order.dao.LeOrderOperateLogMapper;
import com.zz.order.feign.BusinessService;
import com.zz.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Many;
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

	int orderSeconds = 300;


	@Transactional
	public ResponseResult createOrder(int uid, int pid, HttpServletRequest request){
		int id = pid;
		GetLeProductPicMenuExtResult productResult = businessService.getProductById(id);
		LeProduct leProduct = productResult.getLeProductPicMenuExt().getLeProduct();
		if (leProduct == null ){
			return  new ResponseResult(ProductCode.PRODUCT_NOTCOMPLETE);
		}
		if (leProduct.getIssale() == 3){
			//今日已售
			return  new ResponseResult(OrderCode.ORDER_SALED);
		}
		Long expire = null;
		try {
			//先查redis存不存在
			String key = "order_" + pid;
			String value = stringRedisTemplate.opsForValue().get(key);
			if (StringUtils.isNotBlank(value)){
				//不存在 则可以下单
				value = "uid = " + uid ;
				stringRedisTemplate.boundValueOps(key).set(value,orderSeconds, TimeUnit.SECONDS);
				//修改商品状态为销售中
				ResponseResult responseResult = businessService.updateProductIsSaleByPid(pid, 4,request);
				if (responseResult.getCode() == 10000){
					//下订单
					LeOrder order = LeOrder.builder().bid(leProduct.getBid())
							.pid(pid)
							.uid(uid)
							.price(leProduct.getBargainprice())
							.createDate(LocalDate.now())
							.createTime(LocalDateTime.now())
							.updateTime(LocalDateTime.now())
							.status(0) //创建
							.build();
					int insert = leOrderMapper.insert(order);
					if (insert > 0){
						LeOrder localOrder = leOrderMapper.getOne(uid, pid, LocalDate.now());
						//记录日志
						String logContent = "uid=" + uid + "用户创建了订单["+localOrder.getId()+"],额度=" + localOrder.getPrice();
						log.info(logContent);
						LeOrderOperateLog log = LeOrderOperateLog.builder().oid(localOrder.getId())
								.content(logContent)
								.uid(uid)
								.createTime(LocalDateTime.now())
								.updateTime(LocalDateTime.now())
								.build();
						operateLogMapper.insert(log);
						return  new ResponseResult(CommonCode.SUCCESS);
					}else {
						return new ResponseResult(CommonCode.FAIL);
					}
				}else {
					return new ResponseResult(CommonCode.FAIL);
				}
			}else {
				//存在
				return new ResponseResult(OrderCode.ORDER_PROCESSING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
		}
		return new ResponseResult(CommonCode.FAIL);
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
}
