package com.yy.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.yy.common.bean.Event;
import com.yy.common.bean.Reply;
import com.yy.common.bean.WXURL;
import com.yy.common.enums.ReturnInfo;
import com.yy.common.exception.ResultException;
import com.yy.common.redis.RedisDao;
import com.yy.entity.Account;
import com.yy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Package: com.yy.common.util
 * @ClassName: WXUtil
 * @Author: Created By Yy
 * @Date: 2019-05-29 15:29
 */
@Slf4j
public class WXUtil {
    private Account account;
    private RestTemplate restTemplate;
    private RedisDao redisDao;
    private UserService userService;

    public WXUtil(Account account, RestTemplate restTemplate, RedisDao redisDao,UserService userService) {
        this.account = account;
        this.restTemplate = restTemplate;
        this.redisDao = redisDao;
        this.userService=userService;

    }

    /**
     * 认证回调
     * @param request
     * @param response
     */
    public void authorize(HttpServletRequest request, HttpServletResponse response) {

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
                //获取返回的数据
                String xmlInfo = XMLUtil.getXMLInfo(request);
                String ip = IPUtil.getIp(request);
                //解析数据
                Event event=XMLUtil.parseXML(xmlInfo,ip);
                //更新用户
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(()->{
                   String userInfo = getUserInfo(event.getUserOpenid());
                    userService.addOrUpdateUser(userInfo);
                });
                //返回消息
                String responseXML=null;
                Reply reply=new Reply();
                if (Reply.MsgType.text.name().equals(event.getMsgType())){
                    Reply.Content content=new Reply.Content();
                    content.setContent(chat(event.getUserOpenid(),event.getText().getContent()));
                    reply.setContent(content);
                    responseXML = XMLUtil.getResponseXML(event, Reply.MsgType.text, reply);
                    System.out.println(responseXML);
                }else if ("event".equals(event.getMsgType())){
                    List<Reply.News> news=new ArrayList<>();
                    Reply.News new1=new Reply.News();
                    new1.setUrl("http://baidu.com");
                    new1.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/icELdWDKoXUXdrLOsD7pT2WU0TB3M9HrrcDOGvxO1lv8ibYhAbRcTPoHJicxlfpPm8FHybYyZJTeuhYJULZpLzicnQ/0?wx_fmt=png");
                    new1.setTitle("第一个图");
                    new1.setDescription("\uD83C\uDF3A\uD83C\uDF3B\uD83C\uDF3C\uD83C\uDF37");
                    Reply.News new2=new Reply.News();
                    new2.setUrl("http://baidu.com");
                    new2.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/icELdWDKoXUXdrLOsD7pT2WU0TB3M9HrrcDOGvxO1lv8ibYhAbRcTPoHJicxlfpPm8FHybYyZJTeuhYJULZpLzicnQ/0?wx_fmt=png");
                    new2.setTitle("\uD83C\uDE33㊗㊙");
                    new2.setDescription("\uD83C\uDE33㊗㊙");
                    news.add(new1);
                    news.add(new2);
                    reply.setNews(news);
                    responseXML = XMLUtil.getResponseXML(event, Reply.MsgType.news, reply);
                }
                //输出
                outPut(response,responseXML);


            }
        }
    }

    /**
     * 获取二维码
     * @return
     */
    public Map<String,Object> getQr(Integer expireTime, String senceID){
        //查看二维码是否过期
        Map<String, Object> map = existQr();
        if (CollUtil.isNotEmpty(map)){
            return map;
        }
        String str;
        if (NumberUtil.isNumber(senceID)) {
            str = "{\"expire_seconds\": " + expireTime + ", \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": " + senceID + "}}}";
        }else {
            str = "{\"expire_seconds\": " + expireTime + ", \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + senceID + "\"}}}";
        }
        String url= WXURL.qr_post.replace("TOKEN",account.getAccessToken());
        String qr = restTemplate.postForObject(url, str, String.class);
        log.info(qr);
        if (qr.contains("errcode")){
            throw new ResultException(ReturnInfo.businessError.getCode(),"获取二维码出错");
        }
        Map<String,Object> data=(Map)JSONObject.parse(qr);
        redisDao.addQr(data);
        return data;
    }

    /**
     *
     *
     *==================================================================================================================
     *
     *
     *
     *
     *==================================================================================================================
     *
     */
    private String chat(String openid,String content){
        Map<String, Object> params = userService.chat(content, openid);
        String data = HttpUtil.get(WXURL.chat_post, params);
        System.out.println(data);
        Map map = (Map) JSONObject.parse(data);
        int ret = (int) map.get("ret");
        if (ret==0){
            return (String) map.get("data");
        }else {
            return (String)map.get("msg");
        }

    }
    private String getUserInfo(String openid){
        String url= WXURL.user_get.replace("TOKEN",account.getAccessToken())
                .replace("OPENID",openid);
        String data = restTemplate.getForObject(url, String.class);
        if (data.contains("errcode")){
            log.info(data);
            throw new ResultException(ReturnInfo.businessError.getCode(),"获取信息出错");
        }
        return data;
    }

    private Map<String,Object> existQr(){
       return redisDao.getQr();
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
