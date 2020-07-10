package com.zz.framework.domain.aop;


import com.alibaba.fastjson.JSONObject;
import com.zz.framework.domain.system.WebLog;
import com.zz.framework.utils.StrUtil;
import io.swagger.annotations.ApiOperation;
import net.logstash.logback.marker.Markers;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.ClassLoaderUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一日志处理切面
 * Created by macro on 2018/4/26.
 */
@Aspect
@Component
public class WebLogAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);
	private static final String POINTCUT = "execution(public * com.zz.web.controller..*.*(..))" ;
//	@Pointcut("execution(public * com.zz.web.controller..*.*(..))")
	@Pointcut(POINTCUT)
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) throws Exception {
	}

//	@AfterThrowing("webLog()")
//	public void doError(JoinPoint joinPoint) throws Exception{
//		System.out.println("doError");
//	}

	@AfterReturning(value = "webLog()", returning = "ret")
	public void doAfterReturning(Object ret) throws Exception {
	}

	@Around("webLog()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		WebLog webLog = new WebLog();
		long startTime = System.currentTimeMillis();
		//获取当前请求对象
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		assert attributes != null;
		HttpServletRequest request = attributes.getRequest();
		//记录请求信息(通过Logstash传入Elasticsearch)
		try {
			result = joinPoint.proceed();
			webLog.setResult(result);
		} catch (Exception e) {
			webLog.setResult("接口返回数据异常。");
//			exceptioinCatch.exception(e);
			// throw throwable  不加这行代码全局异常将失效
			throw e;
		} finally {
			long endTime = System.currentTimeMillis();
			Signature signature = joinPoint.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();
			if (method.isAnnotationPresent(ApiOperation.class)) {
				ApiOperation log = method.getAnnotation(ApiOperation.class);
				webLog.setDescription(log.value());
			}
			String urlStr = request.getRequestURL().toString();
			webLog.setBasePath(StrUtil.removeSuffix(urlStr, url(urlStr, null).getPath()));
			webLog.setIp(request.getRemoteUser());
			webLog.setMethod(request.getMethod());
			webLog.setParameter(getParameter(method, joinPoint.getArgs()));
			webLog.setSpendTime((int) (endTime - startTime));
			webLog.setStartTime(startTime);
			webLog.setUri(request.getRequestURI());
			webLog.setUrl(request.getRequestURL().toString());
			Map<String, Object> logMap = new HashMap<>();
			logMap.put("url", webLog.getUrl());
			logMap.put("method", webLog.getMethod());
			logMap.put("parameter", webLog.getParameter());
			logMap.put("spendTime", webLog.getSpendTime());
			logMap.put("description", webLog.getDescription());
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(webLog);
			LOGGER.info(Markers.appendEntries(logMap), jsonObject.toString());
		}
		return result;
	}

	/**
	 * 根据方法和传入的参数获取请求参数
	 */
	private Object getParameter(Method method, Object[] args) {
		List<Object> argList = new ArrayList<>();
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			//将RequestBody注解修饰的参数作为请求参数
			RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
			if (requestBody != null) {
				argList.add(args[i]);
			}
			//将RequestParam注解修饰的参数作为请求参数
			RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
			if (requestParam != null) {
				Map<String, Object> map = new HashMap<>();
				String key = parameters[i].getName();
				if (!StringUtils.isEmpty(requestParam.value())) {
					key = requestParam.value();
				}
				map.put(key, args[i]);
				argList.add(map);
			}
		}
		if (argList.size() == 0) {
			return null;
		} else if (argList.size() == 1) {
			return argList.get(0);
		} else {
			return argList;
		}
	}

	/**
	 * 通过一个字符串形式的URL地址创建URL对象
	 *
	 * @param url     URL
	 * @param handler {@link URLStreamHandler}
	 * @return URL对象
	 * @since 4.1.1
	 */
	public URL url(String url, URLStreamHandler handler) throws MalformedURLException {
		/** 针对ClassPath路径的伪协议前缀（兼容Spring）: "classpath:" */
		String classpath = "classpath:";
		if (url.startsWith(classpath)) {
			url = url.substring(classpath.length());
			return getClassLoader().getResource(url);
		}
		return new URL(null, url, handler);

	}

	/**
	 * 获取{@link ClassLoader}<br>
	 * 获取顺序如下：<br>
	 *
	 * <pre>
	 * 1、获取当前线程的ContextClassLoader
	 * 2、获取{@link ClassLoaderUtil}类对应的ClassLoader
	 * 3、获取系统ClassLoader（{@link ClassLoader#getSystemClassLoader()}）
	 * </pre>
	 *
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		;
		if (classLoader == null) {
			classLoader = ClassLoaderUtil.class.getClassLoader();
			if (null == classLoader) {
				classLoader = ClassLoader.getSystemClassLoader();
			}
		}
		return classLoader;
	}
}
