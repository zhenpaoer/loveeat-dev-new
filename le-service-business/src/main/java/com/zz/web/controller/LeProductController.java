package com.zz.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zz.business.feign.PictureService;
import com.zz.business.feign.ProviderService;
import com.zz.business.service.LeProductService;
import com.zz.framework.api.business.ProductControllerApi;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.QueryResponseResult;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.LeProductPicurl;
import com.zz.framework.domain.business.response.BusinessCode;
import com.zz.framework.domain.business.response.GetLeProductPicMenuExtResult;
import com.zz.framework.domain.business.response.ProductCode;
import com.zz.framework.domain.picture.response.PictureCode;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.utils.LeOauth2Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName LeBusinessDetailController
 * @Description: TODO 产品服务
 * @Author zhangzhen
 * @Date 2020/5/17
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("product")
public class LeProductController implements ProductControllerApi {
	@Autowired
	LeProductService leProductService;
	@Autowired
	PictureService pictureService;
	@Autowired
	public ProviderService providerService;
	private Pattern lonPattern = Pattern.compile("^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,5}|1[0-7]?\\d{1}\\.\\d{1,5}|180\\.0{1,5})$");
	private Pattern latPattern = Pattern.compile("^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,5}|90\\.0{1,5})$");
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	//公钥
	private static final String PUBLIC_KEY = "publickey.txt";

	//查找某一个商品所有的信息 包括图片 菜单 商品信息
	@Override
	@GetMapping("/getbyid")
	public GetLeProductPicMenuExtResult getProductById(@RequestParam("id") int id,HttpServletRequest request) {
		LeOauth2Util.UserJwt userJwt = LeOauth2Util.getUserJwtFromHeader(request);
		int uid = 0;
		if(userJwt != null){
			uid = Integer.parseInt(userJwt.getId());
		}
		return leProductService.getLeProduct(id,uid);
	}

	//查询所有商品信息
	@Override
	@GetMapping("/all")
	@PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
	public QueryResponseResult<LeProduct> getAllProduct() {
		return leProductService.getAll();
	}

	//查询首页商品信息
//	productType: String(0),       //商品类型
//	priceType: String(0),         //价格类型
//	sortType: String(0),          //排序规则
	@Override
	@GetMapping("/allforhome")
	public QueryResponseResult<LeProduct> getAllForHome(int pageSize,int pageNo,String lon,String lat,String distance,
														int cityId,int regionId,int areaId,String productType,String priceType, String sortType,HttpServletRequest request) {
		log.info("查询首页商品接口参数 pageSize={},pageNo={},lon={},lat={},distance={},cityId={},regionId={},areaId={},productType={},priceType={},sortType={}",
				pageSize,pageNo,lon,lat,distance,cityId,regionId,areaId,productType,priceType,sortType);
		String uidByToken = getUidByToken(request);
		int uid = 0;
		if (StringUtils.isNotEmpty(uidByToken)){
			uid = Integer.parseInt(uidByToken);
		}
		log.info("查询首页商品接口参数uid={}",uid);
		if (pageSize <= 0 ){
			pageSize = 1;
		}
		if (pageNo <= 0 ){
			pageSize = 1;
		}
		if (StringUtils.isEmpty(lon)){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_LOCATION_ERROR);
		}
		if (StringUtils.isEmpty(lat)){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_LOCATION_ERROR);
		}
		if ((cityId + regionId + areaId) == 0 ){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_AREA_ERROR);
		}
		if ("".equals(productType) || "".equals(priceType) || "".equals(sortType)){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_AREA_ERROR);
		}
		return leProductService.getAllForHome(pageSize,pageNo,lon,lat,distance,cityId,regionId,areaId,productType,priceType, sortType,uid);
	}

	@Override
	@PostMapping("saveproduct")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult saveProduct(LeProduct leProduct, int bid) {
		if (bid < 0 ){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_BID_FALSE);
		}
		if (leProduct == null){
			ExceptionCast.cast(ProductCode.PRODUCT_PARAM_NULL);
		}
		if ("".equals(leProduct.getDescrib())) {
			ExceptionCast.cast(ProductCode.PRODUCT_DISCRI_NULL);
		}
		if ("".equals(leProduct.getBusinessname())) {
			ExceptionCast.cast(ProductCode.PRODUCT_BUSINESSNAME_NULL);
		}
		if ("".equals(leProduct.getProductname())) {
			ExceptionCast.cast(ProductCode.PRODUCT_NAME_NULL);
		}
		if (leProduct.getOriginalprice().equals(new BigDecimal(0))) {
			ExceptionCast.cast(ProductCode.PRODUCT_PRICE_NULL);
		}
		if (leProduct.getFoodtype() == 0) {
			ExceptionCast.cast(ProductCode.PRODUCT_FOODTYPE_NULL);
		}
		return leProductService.saveProduct(leProduct,bid);
	}

	@Override
	@PostMapping("saveproductmenu")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails, int pid) {
		if (pid < 0){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		return leProductService.saveProductMenu(leProductMenudetails,pid);
	}
//, consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	@Override
	@PostMapping(value = "saveproductpic")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult saveProductPic(@RequestPart MultipartFile file,@RequestParam String pid) {
//		String pid = request.getParameter("pid");
		if (Integer.parseInt(pid) < 0){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		ResponseResultWithData result = pictureService.uploadProPic(file, pid);
		if (result == null ) ExceptionCast.cast(PictureCode.PICTURE_UPLOAD_ERROR);
		int code = result.getCode();
		if (code != 10000){
			return new ResponseResult(PictureCode.PICTURE_UPLOAD_ERROR);
		}
		HashMap<String, Object> data = result.getData();
		String picUrl = (String)data.get("downFileUrl");
		LeProductPicurl leProductPicurl = new LeProductPicurl();
		leProductPicurl.setPicurl(picUrl);
		leProductPicurl.setPid(Integer.parseInt(pid));
		return leProductService.saveProductPic(leProductPicurl,Integer.parseInt(pid) );
	}

	@Override
	@GetMapping("delproductpic")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_BUSINESS')")
	public ResponseResult delProductPic(String pid, String url) {
		if(StringUtils.isEmpty(pid) || StringUtils.isEmpty(url)){
			ExceptionCast.cast(BusinessCode.BUSINESS_CHECK_ID_FALSE);
		}
		return leProductService.delBusinessPic(pid, url);

	}

	//对商品砍价
	@Override
	@GetMapping("/bargain")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseResultWithData bargainByPid(int pid,HttpServletRequest request) {
		LeOauth2Util.UserJwt userJwt = LeOauth2Util.getUserJwtFromHeader(request);
		if (userJwt == null){
			ExceptionCast.cast(AuthCode.AUTH_LOGIN_ERROR);
		}
		assert userJwt != null;
		String uid = userJwt.getId();
//		String uid = getUidByToken(request);
		if (pid <= 0){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		if (StringUtils.isEmpty(uid) || Integer.parseInt(uid) <= 0){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_ID_FALSE);
		}
		return leProductService.bargain(pid,Integer.parseInt(uid)) ;
	}

	@Override
	@PostMapping("/updateproductissale")
	@PreAuthorize(value="isAuthenticated() and  hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public ResponseResult updateProductIsSaleByPid(int pid, int issale) {
		if (pid <= 0){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		if (issale <= 0){
			ExceptionCast.cast(ProductCode.PRODUCT_ISSALE_ERROR);
		}
		return leProductService.updateProductIsSaleByPid(pid,issale);
	}

	@Override
	@GetMapping("/updateProductIsSaleToOne")
	public ResponseResult updateProductIsSaleToOne(@RequestParam("pid")int pid) {
		if (pid <= 0){
			ExceptionCast.cast(ProductCode.PRODUCT_CHECK_PID_FALSE);
		}
		return leProductService.updateProductIsSaleToOne(pid);
	}

	//测试服务提供者
	@GetMapping("/hi")
	public String hi(@RequestParam("msg") String msg){
		System.out.println(11);
		String hi = providerService.hi(msg);
		if (StringUtils.isNotEmpty(hi)) return hi;
		return "111";
	}

	/**
	 * 校验经度 对则返回true 错返回false
	 *
	 * @param lon
	 * @return
	 */
	private boolean checkLon(String lon) {
		return (lonPattern.matcher(lon).matches());
	}

	/**
	 * 校验纬度 对则返回true 错返回false
	 *
	 * @param lat
	 * @return
	 */
	private boolean checkLat(String lat) {
		return (latPattern.matcher(lat).matches());
	}

	private String getUidByToken(HttpServletRequest request){
		AuthToken authToken = new AuthToken();
		String token = request.getHeader("token");
		//根据令牌从redis查询jwt
		try {
			if (StringUtils.isEmpty(token)){
				return "";
			}
			String key = "token:"+token;
			String value = stringRedisTemplate.opsForValue().get(key);
			if(value !=null){
				authToken =  JSON.parseObject(value, AuthToken.class);
			}else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RedisConnectionUtils.unbindConnection(Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()));
		}
		String jwt_token = authToken.getJwt_token();
		Jwt jwt = JwtHelper.decodeAndVerify(jwt_token, new RsaVerifier(Objects.requireNonNull(getPubKey())));
		String claims = jwt.getClaims();
		JSONObject jsonObject = JSONObject.parseObject(claims);
		return jsonObject.getString("id");
	}

	/*** 获取非对称加密公钥 Key * @return 公钥 Key */
	private String getPubKey() { Resource resource = new ClassPathResource(PUBLIC_KEY);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
			BufferedReader br = new BufferedReader(inputStreamReader);
			return br.lines().collect(Collectors.joining("\n"));
		} catch (IOException ioe) {
			return null;
		}
	}
}
