package com.yy;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Package: com.yy
 * @ClassName: Down
 * @Author: Created By Yy
 * @Date: 2019-06-01 09:21
 */
public class Down {
    private static String appid="1106958047";
    private static String appkey="mDmA9oiD9hCeHpbM";
    public static void main(String[] args) throws Exception {
        String url="https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat";
        Map<String,Object> params=new HashMap<>();
        params.put("session",IdUtil.simpleUUID());
        params.put("question","你傻逼");
        String s = HttpUtil.get(url, getSign(params));
        System.out.println(s);


    }
    public static Map<String,Object> getSign(Map<String,Object> params) throws Exception {
        Map<String,Object> param=new TreeMap<>(params);
        param.put("app_id",appid);
        param.put("time_stamp",System.currentTimeMillis()/1000+"");
        param.put("nonce_str", IdUtil.simpleUUID());
        StringBuilder builder=new StringBuilder();
        for (Map.Entry map:param.entrySet()){
            builder.append(map.getKey()).append("=");
            builder.append(URLEncoder.encode(map.getValue().toString(),"UTF-8")).append("&");
        }
        builder.append("app_key=").append(appkey);
        String sign = SecureUtil.md5(builder.toString()).toUpperCase();
        param.put("sign",sign);
        return param;
    }
}
