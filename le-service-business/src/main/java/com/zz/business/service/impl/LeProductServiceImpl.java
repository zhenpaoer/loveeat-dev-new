package com.zz.business.service.impl;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zz.business.dao.LeProductMapper;
import com.zz.business.dao.LeProductPicurlMapper;
import com.zz.business.feign.PictureService;
import com.zz.business.service.*;
import com.zz.framework.common.model.response.*;
import com.zz.framework.domain.business.*;
import com.zz.framework.domain.business.ext.LeProductMenuNode;
import com.zz.framework.domain.business.ext.LeProductPicMenuExt;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.framework.domain.business.response.ProductCode;
import jdk.nashorn.internal.runtime.options.LoggingOption;
import lombok.Data;
import lombok.Synchronized;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.runtime.Log;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName LeProductServiceImpl
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@Service
@Slf4j
public class LeProductServiceImpl implements LeProductService {
	@Autowired
	LeProductMapper leProductMapper;
	@Autowired
	LeProductUrlService leProductUrlService;
	@Autowired
	LeProductMenudetailService leProductMenudetailService;
	@Autowired
	LeBusinessDetailService leBusinessDetailService;
	@Autowired
	PictureService pictureService;
	@Autowired
	LeProductPicurlMapper leProductPicurlMapper;
	@Autowired
	LeBargainLogServiceImpl leBargainLogService;
	@Autowired
	LeBargainRuleService leBargainRuleService;
	public ConcurrentHashMap<Integer,LimitPirce> limitPirceMap = new ConcurrentHashMap<>();




	//查找某一个商品所有的信息 包括图片 菜单 商品信息
	@Override
	public GetLeProductPicMenuExtResult getLeProduct(int id) {
		LeProduct leProduct = leProductMapper.selectByPrimaryKey(id);
		if (leProduct == null ){
			return  new GetLeProductPicMenuExtResult(ProductCode.PRODUCT_NOTCOMPLETE,null);
		}
		List<LeProductMenuNode> productMenuByPid = leProductMenudetailService.getProductMenuByPid(id);
		List<LeProductPicurl> productUrlByPid = leProductUrlService.getProductUrlByPid(id);
		if ( productMenuByPid.size() == 0 || productUrlByPid.size() == 0){
			return  new GetLeProductPicMenuExtResult(ProductCode.PRODUCT_NOTEXIT,null);
		}
		LeProductPicMenuExt leProductPicMenuExt = new LeProductPicMenuExt();
		leProductPicMenuExt.setLeProduct(leProduct);
		leProductPicMenuExt.setProductMenuNodes(productMenuByPid);
		leProductPicMenuExt.setProductPicurls(productUrlByPid);
		GetLeProductPicMenuExtResult productDetail = new GetLeProductPicMenuExtResult(CommonCode.SUCCESS,leProductPicMenuExt);
		return productDetail;
	}

	//查找所有商品
	@Override
	public QueryResponseResult<LeProduct> getAll() {
		List<LeProduct> allProductForUser = leProductMapper.getAllProductForUser();
		QueryResult<LeProduct> QueryResult = new QueryResult<LeProduct>();
		QueryResult.setList(allProductForUser);
		QueryResult.setTotal(allProductForUser.size());
		QueryResponseResult<LeProduct> responseResult = new QueryResponseResult<>(CommonCode.SUCCESS,QueryResult);
		return responseResult;
	}

	//给首页查找所有商品
	@Override
	public QueryResponseResult<LeProduct> getAllForHome(int pageSize,int pageNo,String lon,String lat,String distance,int cityId,int regionId,int areaId
														,String productType,String priceType, String sortType,int uid) {
		//筛选出来商家
		List<LeBusinessDetail> businessByAreaConditions = leBusinessDetailService.getBusinessByAreaConditions(cityId, regionId, areaId);
		List<Integer> bids = businessByAreaConditions.parallelStream().map(LeBusinessDetail::getId).collect(Collectors.toList());
		log.info("首页获取商家ids={}",bids);
		if (bids.size() == 0 ){
			return new QueryResponseResult<>(CommonCode.SUCCESS,new QueryResult());
		}
		PageHelper.startPage(pageNo, pageSize);

		HashMap<String,Object> map = new HashMap<>();
		map.put("lon",lon);
		map.put("lat",lat);
		map.put("distance",distance);
		map.put("ids",bids);
		if (!"0".equals(productType)){
			map.put("productType",Integer.parseInt(productType));
		}else {
			map.put("productType",null);
		}
		//价格类型
//		type 0:全部 1: 50元以下 2:50-100 3:100-150 4:150-200  5:200元以上
		int type = Integer.parseInt(priceType);
		BigDecimal priceStart = null;
		BigDecimal priceEnd = null;
		if (type == 1){
			priceEnd = new BigDecimal(50);
		}else if (type == 2 ){
			priceStart = new BigDecimal(50);
			priceEnd = new BigDecimal(100);
		} else if (type == 3 ){
			priceStart = new BigDecimal(100);
			priceEnd = new BigDecimal(150);
		}else if (type == 4 ){
			priceStart = new BigDecimal(150);
			priceEnd = new BigDecimal(200);
		}else if (type == 5 ){
			priceStart = new BigDecimal(200);
			priceEnd = new BigDecimal(10000);
		}else {
			priceStart = new BigDecimal(0);
			priceEnd = new BigDecimal(10000);
		}
		map.put("priceStart",priceStart);
		map.put("priceEnd",priceEnd);
		//排序
//		sortType 0:智能排序 1：好评优先，2距离优先 3价格低到高 4价格高到低
		int sorttype = Integer.parseInt(sortType);
//		<if test="orderBy != null">
//				order by
//				<choose>
//        <when test='orderBy=="age"'>age</when>
//        <when test='orderBy=="size"'>size</when>
//        <when test='orderBy=="price"'>price</when>
//        <otherwise> ${orderBy} </otherwise>
//    </choose>
//</if>
		if (sorttype == 1){ //加评分系统 字段 降序

		}else if (sorttype == 2){
			map.put("sortType","distance"); //升序
		}else if (sorttype == 3){
			map.put("sortType","bargainprice asc"); //升序
		}else if (sorttype == 4){
			map.put("sortType","bargainprice desc"); //降序
		}else {
			//距离最近 价格低到高
			map.put("sortType","distance asc,bargainprice asc"); //降序
			log.info("我这里是智能排序");
		}

		List<LeProduct> leProducts = leProductMapper.getHomeProduct(map);
		List<Integer> pids = leProducts.parallelStream().map(LeProduct::getId).collect(Collectors.toList());
		log.info("首页获取商品ids={}",pids);
		if (pids.size() == 0 ){
			return new QueryResponseResult<>(CommonCode.SUCCESS,new QueryResult());
		}
		List<Integer> hasBarginPidsToday = new ArrayList<>();
		if (uid != 0){
			List<LeBargainLog> listByPidUidDate = leBargainLogService.getListByPidUidDate(pids, uid, LocalDate.now().toString());
			if (listByPidUidDate.size() >0){
				hasBarginPidsToday = listByPidUidDate.parallelStream().map(LeBargainLog::getPid).collect(Collectors.toList());
			}
		}
		log.info("首页获取已砍价记录pids={}",hasBarginPidsToday);
		//修改距离显示
		List<Integer> finalHasBarginPidsToday = hasBarginPidsToday;
		leProducts.stream().forEach(item -> {
			if (finalHasBarginPidsToday.size() > 0){
				if (item.getIssale() != 3 && finalHasBarginPidsToday.contains(item.getId())){
					item.setIssale(2);
				}
			}
			double distance1 = Math.floor((Double.parseDouble(item.getDistance())));
			double distance2 = Double.parseDouble(item.getDistance()) ;
			if (distance1 < 1){
				item.setDistance((int)Math.floor(distance2 * 1000)+"m"); //取整数
			}else {
				item.setDistance((double) Math.round(distance2 * 100) / 100+"km");
			}
		});
		PageInfo pageInfo = new PageInfo(leProducts);
		QueryResult<LeProduct> QueryResult = new QueryResult<LeProduct>();
		QueryResult.setList(leProducts);
		QueryResult.setPageNo(pageNo);
		QueryResult.setPageSize(pageSize);
		QueryResult.setTotal(leProducts.size());
		QueryResponseResult<LeProduct> responseResult = new QueryResponseResult<>(CommonCode.SUCCESS,QueryResult);

		return responseResult;
	}

	//创建商品信息
	@Override
	public ResponseResult createLeProduct(LeProduct leProduct) {
		int result = leProductMapper.insert(leProduct);
		if (result == 1){
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(ProductCode.PRODUCT_CREATE_FALSE);
	}

	//保存商家的商品主要信息
	@Override
	public ResponseResult saveProduct(LeProduct leProduct, int bid) {
		try{
			//根据商家id查询是否存在
			LeBusinessDetail leBusinessDetail = leBusinessDetailService.getLeBusinessDetail(bid);
			if (leBusinessDetail == null){
				return new ResponseResult(ProductCode.PRODUCT_BID_NOTEXIT);
			}
			leProduct.setBid(bid);
			if ( leProduct.getId()== null || leProduct.getId() == 0){
				//根据商品名称查询 此商品是否存在了
				Example example = new Example(LeProduct.class);
				Example.Criteria criteria = example.createCriteria();
				criteria.andEqualTo("productname",leProduct.getProductname());
				List<LeProduct> leProducts = leProductMapper.selectByExample(example);
				//商品名称存在
				if(leProducts.size() > 0){
					return new ResponseResult(ProductCode.PRODUCT_NAME_EXIT);
				}
				//商品名称不存在，创建新商品
				return createLeProduct(leProduct);
			} else {
				//商品存在需要更新，先查库
				LeProduct localLeProduct = leProductMapper.selectByPrimaryKey(leProduct.getId());
				localLeProduct.setBusinessname(leProduct.getBusinessname());
				localLeProduct.setProductname(leProduct.getProductname());
				localLeProduct.setDescrib(leProduct.getDescrib());
				localLeProduct.setFoodtype(leProduct.getFoodtype());
				localLeProduct.setOriginalprice(leProduct.getOriginalprice());
				localLeProduct.setUpdatetime(LocalDateTime.now());
				localLeProduct.setState(2);
				localLeProduct.setStatus("审核中");
				int update = leProductMapper.updateByPrimaryKey(localLeProduct);
				if (update == 0){
					return new ResponseResult(ProductCode.PRODUCT_UPDATE_ERROR);
				}
				return new ResponseResult(CommonCode.SUCCESS);
			}
		}catch (Exception e){
			System.out.println(e);
		}
		return new ResponseResult(ProductCode.PRODUCT_OTHER_ERROR);
	}
	//保存商家的商品菜单信息
	@Override
	public ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails, int pid) {
		if (leProductMenudetails.size() != 0 && leProductMenudetails.size() != 1){
			//先查pid
			LeProduct leProduct = leProductMapper.selectByPrimaryKey(pid);
			if (leProduct == null){
				return new ResponseResult(ProductCode.PRODUCT_NOTEXIT);
			}
			leProductMenudetails.parallelStream().forEach(item -> item.setPid(pid));
			return leProductMenudetailService.saveProductMenu(leProductMenudetails);
		}else {
			return  new ResponseResult(ProductCode.PRODUCT_CHECK_MENU_ERROR);
		}

	}

	@Override
	public ResponseResult saveProductPic(LeProductPicurl leProductPicurl, int pid) {
		if (leProductPicurl  != null ){
			//先查pid
			LeProduct leProduct = leProductMapper.selectByPrimaryKey(pid);
			if (leProduct == null){
				return new ResponseResult(ProductCode.PRODUCT_NOTEXIT);
			}
			//批量更新图片记录
//			return leProductUrlService.saveLeProductPicurls(leProductPicurls);
			//更新某一个图片记录
			return leProductUrlService.saveLeProductPicurl(leProductPicurl);
		}else {
			return  new ResponseResult(ProductCode.PRODUCT_CHECK_MENU_ERROR);
		}
	}

	//删除商品图片
	@Override
	public ResponseResult delBusinessPic(String pid, String url) {

		LeProductPicurl leProductPicurl = leProductPicurlMapper.selectByPrimaryKey(pid);
		if (leProductPicurl != null && StringUtils.isNotEmpty(leProductPicurl.getPicurl())){
			if (url.equals(leProductPicurl.getPicurl())){
				boolean delResult = pictureService.removePic(url);
				int delPicResult = leProductPicurlMapper.deleteByPrimaryKey(pid);
				if (delResult && delPicResult > 0) return new ResponseResult(CommonCode.SUCCESS);
			}
		}
		return new ResponseResult(ProductCode.PRODUCT_DELETE_PICTURE_FALSE);
	}

	//砍价
	@Override
	@Transactional
	public synchronized  ResponseResultWithData  bargain(int pid,int uid) {
		LeProduct leProduct = leProductMapper.selectByPrimaryKey(pid);
		BigDecimal needBargainPrice = null;
		BigDecimal afterBargainPrice =null;
		if (leProduct != null){
			ArrayList<Integer> pids = new ArrayList<>();
			pids.add(pid);
			List<LeBargainLog> listByPidUidDate = leBargainLogService.getListByPidUidDate(pids, uid, LocalDate.now().toString());
			if (listByPidUidDate.size()>0){
				return new ResponseResultWithData(ProductCode.PRODUCT_BARGAIN_AGAIN_ERROR,null);
			}
			BigDecimal originalprice = leProduct.getOriginalprice(); //原价
			BigDecimal bargainprice = leProduct.getBargainprice();//砍完之后的价格
//			int beforeBargainPersonSum = leProduct.getBargainpersonsum();//砍价人数
			needBargainPrice = getBargainPrice(originalprice, bargainprice);
			afterBargainPrice = bargainprice.subtract(needBargainPrice);
			//更新商品的砍价记录
			int updateResult = leProductMapper.updateBargainPrice(leProduct.getId(), afterBargainPrice);
			log.info("砍价===更加商品记录boolean:{}",updateResult);
			//插入日志记录
			LeBargainLog bargainLog = new LeBargainLog();
			bargainLog.setPid(pid);
			bargainLog.setPrice(needBargainPrice);
			bargainLog.setUid(uid);
			bargainLog.setCreatetime(LocalDateTime.now());
			bargainLog.setUpdatetime(LocalDateTime.now());
			int insert = leBargainLogService.insert(bargainLog);
			log.info("砍价===插入记录boolean:{}",insert);
			log.info("砍价===本次砍了{},砍前：{}，砍后：{}",needBargainPrice,bargainprice,afterBargainPrice);
			LeProduct leProductNew = leProductMapper.selectByPrimaryKey(pid);
			HashMap<String,Object> data = new HashMap<>();
			data.put("bargainPrice",needBargainPrice);
			data.put("afterBargainPrice",leProductNew.getBargainprice());
			data.put("bargainPerson",leProductNew.getBargainpersonsum());
			return new ResponseResultWithData(CommonCode.SUCCESS,data);
		}else {
			log.info("砍价===商品不存在");
			return new ResponseResultWithData(CommonCode.FAIL,null);
		}

	}

	//根据原价格与砍价后价格的对比 得出百分比 在获取对应的
	public  BigDecimal getBargainPrice(BigDecimal originalprice, BigDecimal bargainprice){
		log.info("砍价==原价:{}，剩余价格：{}",originalprice,bargainprice);
		BigDecimal bigDecimal = bargainprice.multiply(new BigDecimal(10)).divide(originalprice,0, BigDecimal.ROUND_DOWN);
		int discount = bigDecimal.intValue();
		log.info("砍价==折扣:{}",discount);
		LimitPirce limitPirce = limitPirceMap.get(discount);
		if (limitPirce == null){
			loadPriceLimit();
		}
		limitPirce = limitPirceMap.get(discount);
		BigDecimal limitstart = limitPirce.getLimitstart();
		BigDecimal limitend = limitPirce.getLimitend();
		BigDecimal bargainPrice = getRoundPriceByLimit(limitstart.multiply(new BigDecimal(100)).intValue()
													,limitend.multiply(new BigDecimal(100)).intValue());
		log.info("砍价==bargainPrice:{}",bargainPrice);
//		BigDecimal afterBargainPrice = bargainprice.subtract(bargainPrice);
		return bargainPrice;
	}
	public BigDecimal getRoundPriceByLimit(int min,int max){
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		BigDecimal price = new BigDecimal(s).divide(new BigDecimal(100),2,BigDecimal.ROUND_UP);

		return price;
	}

	public static void main(String[] args) {
		BigDecimal a = new BigDecimal("0.05");
		BigDecimal b = new BigDecimal("1");
		int min = a.multiply(new BigDecimal(100)).intValue(); //100
		int max = b.multiply(new BigDecimal(100)).intValue();
		log.info("砍价价格限制 start:{}，end:{}",min,max);
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		log.info("砍价价格s:{}",s);
		BigDecimal price = new BigDecimal(s).divide(new BigDecimal(100),2,BigDecimal.ROUND_UP);
		log.info("砍价价格price:{}",price);
		//根据两个价格 去中间随机一个价格
//		BigDecimal bargainPrice = getBargainPrice(a,b);
	}

	//吧数据库中的砍价规则放入map中
	public void loadPriceLimit(){
		List<LeBargainRule> all = leBargainRuleService.getAll();
		for (LeBargainRule item : all) {
			int discount = item.getDiscount();
			LimitPirce limitPirce = new LimitPirce(item.getLimitstart(),item.getLimitend());
			limitPirceMap.put(discount, limitPirce);//这个方法在key不存在的时候加入一个值,如果key存在就不放入
		}
		log.info("limitPirceMap加载完毕"+limitPirceMap.toString());
	}

	@Data
	@ToString
	private  class LimitPirce {
		private BigDecimal limitstart;
		private BigDecimal limitend;
		LimitPirce(BigDecimal args1,BigDecimal args2){
			this.limitstart = args1;
			this.limitend = args2;
		}
	}
}
