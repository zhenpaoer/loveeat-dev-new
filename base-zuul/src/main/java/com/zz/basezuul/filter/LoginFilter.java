package com.zz.basezuul.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.filter.TokenFilter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zz.basezuul.service.AuthService;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** 身份校验过虑器
 * @author Administrator
 * @version 1.0
 **/

@Component
public class LoginFilter extends ZuulFilter {
    //日志
    private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    private static ArrayList<String> IGNORE_URIS = new ArrayList<>();

    @Autowired
    AuthService authService;

    static {
        IGNORE_URIS.add("/api/provider/hi");
        IGNORE_URIS.add("/api/business/product/allforhome");
        IGNORE_URIS.add("/api/business/product/getbyid");
        IGNORE_URIS.add("/api/business//product/hi");
        IGNORE_URIS.add("/api/business/businessdetail/getBusDeById");
        IGNORE_URIS.add("/api/business/businessdetail/getBusDeList");
        IGNORE_URIS.add("/api/business/business/getBusById");
    }
    //过虑器的类型
    @Override
    public String filterType() {
        /**
         pre：请求在被路由之前执行
         routing：在路由请求时调用
         post：在routing和errror过滤器之后调用
         error：处理请求时发生错误调用
         */
        return "pre";
    }

    //过虑器序号，越小越被优先执行
    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //返回true表示要执行此过虑器
        String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
        logger.info("shouldFilter Uri:",requestURI);
        if (IGNORE_URIS.contains(requestURI)){
            logger.info("shouldFilter false");
            return false;
        }
        logger.info("shouldFilter true");
        return true;
    }

    //过虑器的内容
    //测试的需求：过虑所有请求，判断头部信息是否有Authorization，如果没有则拒绝访问，否则转发到微服务。
    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到request
        HttpServletRequest request = requestContext.getRequest();
        //得到response
        HttpServletResponse response = requestContext.getResponse();

        //从header中取jwt
        String jwtFromHeader = authService.getJwtFromHeader(request);
        //从header中取access_token
        String tokenFromHeader = authService.getTokenFromHeader(request);
        if(StringUtils.isEmpty(jwtFromHeader)){
            //拒绝访问
            access_denied();
            return null;
        }
        //从redis取出jwt的过期时间
        long expire = authService.getExpire(tokenFromHeader);
        if(expire<0){
            //拒绝访问
            access_denied();
            return null;
        }
        return null;
    }


    //拒绝访问
    private void access_denied(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到response
        HttpServletResponse response = requestContext.getResponse();
        //拒绝访问
        requestContext.setSendZuulResponse(false);
        //设置响应代码
        requestContext.setResponseStatusCode(200);
        //构建响应的信息
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        //转成json
        String jsonString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(jsonString);
        //转成json，设置contentType
        response.setContentType("application/json;charset=utf-8");
    }


}
