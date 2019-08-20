package com.yy.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.nio.charset.Charset;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Package: com.yy.common.util
 * @ClassName: OkHttpUtil
 * @Author: Created By Yy
 * @Date: 2019-08-16 11:34
 */
@Slf4j
public class OkHttpUtil {

    private static OkHttpClient client = new OkHttpClient();
    //=============================================get  string==========================================================
    public String doGetString(String url) throws Exception {
        return doGetString(url, null);
    }

    public String doGetString(String url, Map<String, String> params) throws Exception {
        return doGetString(url, params, null);
    }

    public String doGetString(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        return doGetString(url, params, header, null);
    }

    public String doGetString(String url, Map<String, String> params, Map<String, String> header, IdentityHashMap<String, String> addHeader) throws Exception {
        return doGet(url, params, header, addHeader).string();
    }
    //==================================================post string=====================================================
    public String doPostString(String url) throws Exception {
        return doPostString(url, null);
    }

    public String doPostString(String url, Map<String, String> params) throws Exception {
        return doPostString(url, params, null);
    }

    public String doPostString(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        return doPostString(url, params, header,null);
    }

    public String doPostString(String url, Map<String, String> params, Map<String, String> header, IdentityHashMap<String, String> addHeader) throws Exception {
        return doPost(url, params, header, addHeader).string();
    }
    //==================================================do get==========================================================
    private ResponseBody doGet(String url) throws Exception {
        return doGet(url, null);
    }

    private ResponseBody doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, params, null);
    }

    private ResponseBody doGet(String url, Map<String, String> params, Map<String, String> header) throws Exception {
        return doGet(url, params, header, null);
    }

    private ResponseBody doGet(String url, Map<String, String> params, Map<String, String> header, IdentityHashMap<String, String> addHeader) throws Exception {
        StringBuilder builder = new StringBuilder(url);
        if (CollUtil.isNotEmpty(params)) {
            builder.append("?");
            params.forEach((k, v) -> {
                builder.append(k).append("=").append(v).append("&");
            });
            builder.deleteCharAt(builder.length() - 1);
        }
        return getBody(builder.toString(), null, false, header, addHeader);
    }

    //==================================================do post==========================================================
    private ResponseBody doPost(String url) throws Exception{

        return doPost(url, null);
    }

    private ResponseBody doPost(String url, Map<String, String> params) throws Exception{

        return doPost(url, params, null);
    }

    private ResponseBody doPost(String url, Map<String, String> params, Map<String, String> header) throws Exception{

        return doPost(url, params, header,null);
    }

    private ResponseBody doPost(String url, Map<String, String> params, Map<String, String> header, IdentityHashMap<String, String> addHeader) throws Exception{
        RequestBody body = getRequestBody(params);
        return getBody(url,body,true,header,addHeader);
    }

    private RequestBody getRequestBody(Map<String,String> map){
        FormBody.Builder builder = new FormBody.Builder();
        if (CollUtil.isEmpty(map)){
            return builder.build();
        }
        map.forEach((k,v)->{
            builder.addEncoded(k,v);
        });
        return builder.build();
    }

    private ResponseBody getBody(String url, RequestBody body, boolean isPost, Map<String, String> header, IdentityHashMap<String, String> addHeader) throws Exception {
        ResponseBody responseBody = null;
        String msg = null;

        Request request = getRequest(url, body, isPost, header, addHeader);
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            responseBody = response.body();
        } else {
            if (Objects.nonNull(response)) {
                msg = response.body().string();
                throw new Exception(msg);
            }
        }

        return responseBody;
    }

    private Request getRequest(String url, RequestBody body, boolean isPost, Map<String, String> header, IdentityHashMap<String, String> addHeader) throws Exception {
        Request.Builder builder = new Request.Builder();
        URLEncoder encoder = URLEncoder.createDefault();
        url = encoder.encode(url, Charset.forName("UTF-8"));
        builder.url(url);

        if (isPost) {
            builder.post(body);
        }

        if (CollUtil.isNotEmpty(header)) {
            header.forEach((k, v) -> {
                builder.header(k, v);
            });
        }

        if (CollUtil.isNotEmpty(addHeader)) {
            addHeader.forEach((k, v) -> {
                builder.addHeader(k, v);
            });
        }
        return builder.build();
    }
}
