package com.zz.basezuul.service;

import com.zz.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 * 使用StringRedisTemplate查询key的有效期
 **/
@Service
public class AuthService {
    //说明：由于令牌存储时采用String序列化策略，所以这里用 StringRedisTemplate来查询，使用RedisTemplate无 法完成查询。
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //从头取出jwt令牌
    public String getJwtFromHeader(HttpServletRequest request){
        //取出头信息
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            return null;
        }
        if(!authorization.startsWith("Bearer ")){
            return null;
        }
        //取到jwt令牌
        String jwt = authorization.substring(7);
        return jwt;


    }

    //查询令牌的有效期
     public long getExpire(String access_token){
        //key
         Long expire = null;
         try {
             String key = "token:"+access_token;
             expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
         }
         return expire;
     }

    public String getTokenFromHeader(HttpServletRequest request) {
        try {
            //取出头信息
            String token = request.getHeader("token");
            if(StringUtils.isEmpty(token)){
                return null;
            }
            //取到access_token
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisConnectionUtils.unbindConnection(stringRedisTemplate.getConnectionFactory());
        }
        return null;
    }
}
