package com.zz.web.controller;

import com.zz.auth.service.AuthService;
import com.zz.framework.api.auth.AuthControllerApi;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.user.ext.AuthToken;
import com.zz.framework.domain.user.request.LoginRequest;
import com.zz.framework.domain.user.response.AuthCode;
import com.zz.framework.domain.user.response.JwtResult;
import com.zz.framework.domain.user.response.LoginResult;
import com.zz.framework.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by zhangzhen on 2019/7/11
 */
@RestController
@RequestMapping("/")
public class AuthController implements AuthControllerApi {

	@Autowired
	AuthService authService;

	@Value("${auth.clientId}")
	String clientId;

	@Value("${auth.clientSecret}")
	String clientSecret;

	@Value("${auth.cookieDomain}")
	String cookieDomain;

	@Value("${auth.cookieMaxAge}")
	int cookieMaxAge;

	@Override
	@PostMapping("/userlogin")
	public LoginResult login(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		if (username == null || username.equals("")){
			ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
		}
		if (password == null || password.equals("")){
			ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
		}

		//拿到令牌，存到redis
		AuthToken authToken = authService.login(username,password,clientId,clientSecret);
		//将用户身份令牌存到cookie
		this.saveCookie(authToken.getAccess_token());
		return new LoginResult(CommonCode.SUCCESS,authToken.getAccess_token());
	}
	private void saveCookie(String token){

		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		//HttpServletResponse response,String domain,String path, String name,String value, int maxAge,boolean httpOnly
		CookieUtil.addCookie(response,cookieDomain,"/","uid",token,cookieMaxAge,false);
	}

	@Override
	@PostMapping("/userlogout")
	public ResponseResult logout() {
		//取出身份令牌
		String tokenFormCookie = getTokenFormCookie();
		//删除redis中token
		authService.delToken(tokenFormCookie);
		//清除cookie中的token
		clearCookie(tokenFormCookie);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	@Override
	@GetMapping("/userjwt")
	public JwtResult userjwt() {
		//获取cookie中的令牌
		String tokenFormCookie = getTokenFormCookie();
		//根据令牌从redis查询jwt
		AuthToken userToken = authService.getUserToken(tokenFormCookie);
		if (userToken == null){
			return new JwtResult(CommonCode.FAIL,null);
		}

		return new JwtResult(CommonCode.SUCCESS,userToken.getJwt_token());
	}

	private String getTokenFormCookie(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String, String> map = CookieUtil.readCookie(request,"uid");
		String access_token = map.get("uid");
		return access_token;
	}

	private void clearCookie(String token){
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
	}
}
