package com.yy.common.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Package: com.yy.common.util
 * @ClassName: SignUtil
 * @Author: Created By Yy
 * @Date: 2019-05-31 14:07
 */
@Component
public class SignUtil {
    @Value("${qq.appid}")
    private String appid;

    @Value("${qq.appkey}")
    private String appKey;

    public Map<String,Object> getSign(Map<String,Object> params){
        Map<String,Object> param=new TreeMap<>(params);
        param.put("app_id",appid);
        param.put("time_stamp",System.currentTimeMillis()/1000+"");
        param.put("nonce_str", IdUtil.simpleUUID());
        StringBuilder builder=new StringBuilder();
        for (Map.Entry map:param.entrySet()){
            builder.append(map.getKey()).append("=");
            try {
                builder.append(URLEncoder.encode(map.getValue().toString(),"UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        builder.append("app_key=").append(appKey);
        String sign = SecureUtil.md5(builder.toString()).toUpperCase();
        param.put("sign",sign);
        return param;
    }

}
