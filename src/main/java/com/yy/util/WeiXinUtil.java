package com.yy.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.yy.entity.Account;
import com.yy.exception.ResultException;
import com.yy.exception.ReturnInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Package: com.yy.util
 * @ClassName: WeiXinUtil
 * @Author: Created By Yy
 * @Date: 2019-05-09 13:54
 */
@Slf4j
public class WeiXinUtil {
    private Account account;
    private RestTemplate restTemplate;

    public WeiXinUtil(Account account, RestTemplate restTemplate) {
        this.account = account;
        this.restTemplate = restTemplate;
    }

    /**
     * 获取二维码
     * @return
     */
    public Map<String,String> getQr(Integer expireTime,String senceID){
        String str;

        if (NumberUtil.isNumber(senceID)) {
            str = "{\"expire_seconds\": " + expireTime + ", \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + senceID + "}}}";
        }else {
            str = "{\"expire_seconds\": " + expireTime + ", \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": \"" + senceID + "\"}}}";
        }
        String url=WeiXinUrl.qrUrl_post.replace("TOKEN",account.getAccessToken());
        String qr = restTemplate.postForObject(url, str, String.class);
        Map<String,String> data= new HashMap<>();
        JSONObject object = JSONObject.parseObject(qr);
        data.put("ticket",object.getString("ticket"));
        data.put("expire_seconds",object.getString("expire_seconds"));
        data.put("url",object.getString("url"));
        return data;
    }
    /**
     * 认证回调
     * @param request
     * @param response
     */
    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> data = new HashMap<>();
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        //认证
        boolean first = authorizeFirst(timestamp, nonce, account, signature);
        if (first == true) {
            //第一次填写回调接口
            if (StrUtil.isNotBlank(echostr)) {
                outPut(response, echostr);
            } else {

            }
        }
    }

    private boolean authorizeFirst(String timestamp, String nonce, Account account, String signature) {
        boolean flag = true;
        if (StrUtil.isNotBlank(timestamp) && StrUtil.isNotBlank(nonce) && StrUtil.isNotBlank(signature)) {
            List<String> list = new ArrayList<>();
            list.add(timestamp);
            list.add(nonce);
            list.add(account.getToken());
            Collections.sort(list);
            String data = list.stream().collect(Collectors.joining());
            if (!SecureUtil.sha1(data).equals(signature)) {
                flag = false;
            }
        }

        return flag;
    }


    /**
     * 获取token
     *
     * @return
     */
    public String getAccesstoken() {
        if (Objects.isNull(account)) {
            throw new ResultException(ReturnInfo.businessError.getCode(), "无可用公众号");
        }
        String url = WeiXinUrl.tokenUrl_get.replace("APPID", account.getAppID())
                .replace("APPSECRET", account.getAppSecret());
        String dataStr = restTemplate.getForObject(url, String.class);
        JSONObject object = JSONObject.parseObject(dataStr);
        if (object.containsKey("errcode")) {
            throw new ResultException(ReturnInfo.businessError.getCode(), (String) object.get("errmsg"));
        }
        return object.getString("access_token");
    }
    //=======================================================================

    /**
     * 输出
     *
     * @param response
     * @param value
     */
    private void outPut(HttpServletResponse response, String value) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            writer = response.getWriter();
            writer.print(value);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
