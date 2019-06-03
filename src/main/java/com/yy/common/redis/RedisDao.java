package com.yy.common.redis;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.yy.common.redis
 * @ClassName: RedisDao
 * @Author: Created By Yy
 * @Date: 2019-05-29 14:53
 */
@Component
public class RedisDao {
    private static final String token_key="token";
    private static final String qr="qr";
    private static final String ticket="ticket";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取token
     * @return
     */
    public String getAccessToken(){
        String token = (String)redisTemplate.opsForValue().get("token");
        if (StrUtil.isEmpty(token)){
            return null;
        }
        return token;
    }

    /**
     * 添加token
     * @param token
     * @param expire
     * @return
     */
    public void addAccessToken(String token,Integer expire){
        redisTemplate.opsForValue().set(token_key,token, Duration.ofSeconds(expire));
    }

    /**
     * 添加qr
     * @param data
     */
    public boolean addQr(Map<String,Object> data){
        redisTemplate.opsForHash().putAll(qr,data);
       return redisTemplate.expire(qr,(int)data.get("expire_seconds"), TimeUnit.SECONDS);
    }

    /**
     * 获取qr
     * @return
     */
    public Map<String,Object> getQr(){
        return redisTemplate.opsForHash().entries(qr);
    }


}
