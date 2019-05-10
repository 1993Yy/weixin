package com.yy.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.yy.common.bean.Event;
import com.yy.common.bean.MsgType;
import com.yy.common.bean.Reply;
import com.yy.common.bean.WeiXinUrl;
import com.yy.common.exception.ResultException;
import com.yy.common.exception.ReturnInfo;
import com.yy.entity.Account;
import com.yy.entity.User;
import com.yy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * @Package: com.yy.common.util
 * @ClassName: WeiXinUtil
 * @Author: Created By Yy
 * @Date: 2019-05-09 13:54
 */
@Slf4j
public class WeiXinUtil {
    private String key="qr_key";
    private Account account;
    private RestTemplate restTemplate;
    private RedisTemplate redisTemplate;
    private UserService userService;
    public WeiXinUtil(Account account, RestTemplate restTemplate,RedisTemplate redisTemplate,UserService userService) {
        this.account = account;
        this.restTemplate = restTemplate;
        this.redisTemplate=redisTemplate;
        this.userService=userService;
    }

    /**
     * 获取素材
     * @param type
     * @param offset
     * @param count
     * @return
     */
    public Map<String,Object> getMaterialBatch(String type,Integer offset,Integer count){
        String url=WeiXinUrl.materialBatchUrl_post.replace("ACCESS_TOKEN",account.getAccessToken());
        Map<String,Object> params=new HashMap<>();
        params.put("type",type);
        params.put("offset",offset);
        params.put("count",count);
        String dataStr = restTemplate.postForObject(url, JSONObject.toJSONString(params), String.class);
        Map<String, Object> data = (Map<String, Object>) JSONObject.parse(dataStr);
        if (data.containsKey("errcode")){
            throw new ResultException(ReturnInfo.businessError.getCode(),data.get("errcode: ")+""+data.get("errmsg"));
        }
        return data;
    }
    /**
     * 获取用户信息
     * @param openID
     * @return
     */
    public Map<String,String> getUserInfo(String openID){
        String url= WeiXinUrl.userUrl_get.replace("TOKEN",account.getAccessToken())
                .replace("OPENID",openID);
        String dataStr = restTemplate.getForObject(url, String.class);
        JSONObject object = JSONObject.parseObject(dataStr);
        if (object.containsKey("errcode")){
            throw new ResultException(ReturnInfo.businessError.getCode(),object.getString("errcode")+": "+
                    object.getString("errmsg"));
        }
        Map<String,String> data=new HashMap<>();
        data.put("subscribe",object.getString("subscribe"));
        data.put("openid",object.getString("openid"));
        data.put("nickname",object.getString("nickname"));
        data.put("sex",object.getString("sex"));
        data.put("language",object.getString("language"));
        data.put("city",object.getString("city"));
        data.put("province",object.getString("province"));
        data.put("country",object.getString("country"));
        data.put("headimgurl",object.getString("headimgurl"));
        data.put("subscribe_time",object.getString("subscribe_time"));
        data.put("unionid",object.getString("unionid"));
        data.put("remark",object.getString("remark"));
        data.put("groupid",object.getString("groupid"));
        data.put("tagid_list",object.getString("tagid_list"));
        data.put("subscribe_scene",object.getString("subscribe_scene"));
        data.put("qr_scene",object.getString("qr_scene"));
        data.put("qr_scene_str",object.getString("qr_scene_str"));
        return data;
    }
    /**
     * 获取二维码
     * @return
     */
    public Map<String,String> getQr(Integer expireTime,String senceID){
        //查看二维码是否过期
        Map<String, String> map = existQr();
        if (CollUtil.isNotEmpty(map)){
            return map;
        }
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
        if (object.containsKey("errcode")){
            throw new ResultException(ReturnInfo.businessError.getCode(),object.get("errcode")+": "+object.get("errmsg"));
        }
        data.put("ticket",object.getString("ticket"));
        data.put("expire_seconds",object.getString("expire_seconds"));
        data.put("url",object.getString("url"));
        redisTemplate.opsForValue().set(key,JSONObject.toJSONString(data), Duration.ofSeconds(expireTime));
        return data;
    }
    private Map<String,String> existQr(){
        Object o = redisTemplate.opsForValue().get(key);
        if (!Objects.isNull(o)){
            return (Map<String, String>) JSONObject.parse((String)o);
        }
        return null;
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
                //获取微信返回的xml数据
                String xmlInfo = getXMLInfo(request);
                //解析xml数据
                Event event = parseXML(xmlInfo);
                String openID = event.getOpenID();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(()->{
                    Map<String, String> userInfo = getUserInfo(openID);
                    long addUser = userService.addUser(userInfo);
                    log.info("user___"+addUser);
                });
                //组装响应数据
                Reply reply=new Reply();
                Reply.News news = reply.new News();
                news.setDescription("fsdfsdfsd");
                news.setTitle("测试");
                news.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/icELdWDKoXUVn1QQMbJDR3PGAwtS2zFNtWFheBO81mEg5Ox3CtIjibKg2KFEvtYxmO3vwqFvm3ic7SlI0p6Kza6VQ/0?wx_fmt=png");
                news.setUrl("https://mp.weixin.qq.com/s/eybKY87DvDfe8tE5lxqaaQ");
                Reply.News new1 = reply.new News();
                new1.setDescription("fsdfsdfsd");
                new1.setTitle("测试");
                new1.setPicUrl("https://mmbiz.qpic.cn/mmbiz_png/icELdWDKoXUVn1QQMbJDR3PGAwtS2zFNtWFheBO81mEg5Ox3CtIjibKg2KFEvtYxmO3vwqFvm3ic7SlI0p6Kza6VQ/0?wx_fmt=png");
                new1.setUrl("https://mp.weixin.qq.com/s/eybKY87DvDfe8tE5lxqaaQ");
                List<Reply.News> list=new ArrayList<>();
                list.add(news);
                list.add(new1);
                reply.setNews(list);
                String responseXML = getResponseXML(event, MsgType.NEWS, reply);
                //发送数据
                outPut(response,responseXML);

            }
        }
    }
    private String getResponseXML(Event event,String msgType,Reply reply){
        StringBuilder sb = new StringBuilder();

        String wxid = event.getWxID();
        String openid = event.getOpenID();

        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA[").append(openid).append("]]></ToUserName>");
        sb.append("<FromUserName><![CDATA[").append(wxid).append("]]></FromUserName>");
        sb.append("<CreateTime>").append(System.currentTimeMillis()).append("</CreateTime>");
        sb.append("<MsgType><![CDATA[").append(msgType).append("]]></MsgType>");

        if (MsgType.TEXT.equals(msgType)) {

            Reply.Text text = reply.getText();

            sb.append("<Content><![CDATA[").append(text.getContent()).append("]]></Content>");

        } else if (MsgType.IMAGE.equals(msgType)) {
            //图片

            Reply.Image image =reply.getImage();

            sb.append("<Image><MediaId><![CDATA[").append(image.getMediaId()).append("]]></MediaId></Image>");

        } else if (MsgType.VOICE.equals(msgType)) {
            //语音

            Reply.Voice voice =reply.getVoice();

            sb.append("<Voice><MediaId><![CDATA[").append(voice.getMediaId()).append("]]></MediaId></Voice>");

        }else if (MsgType.VIDEO.equals(msgType)) {
            //视频

            Reply.Video video =reply.getVideo();

            sb.append("<Video>");
            sb.append("<MediaId>< ![CDATA[").append(video.getMediaId()).append("] ]></MediaId>");
            sb.append("<Title>< ![CDATA[").append(video.getTitle()).append("] ]></Title>");
            sb.append("<Description>< ![CDATA[").append(video.getDescription()).append("] ]></Description>");
            sb.append("</Video>");

        } else if (MsgType.MUSIC.equals(msgType)) {
            //音乐

            Reply.Music music =reply.getMusic();

            sb.append("<Music>");
            sb.append("<Title><![CDATA["+music.getTitle()+"]]></Title>");
            sb.append("<Description><![CDATA["+music.getDescription()+"]]></Description>");
            sb.append("<MusicUrl><![CDATA["+music.getMusicUrl()+"]]></MusicUrl>");
            sb.append("<HQMusicUrl><![CDATA["+music.getHqMusicUrl()+"]]></HQMusicUrl>");
            sb.append("</Music>");

        } else if (MsgType.NEWS.equals(msgType)) {
            //多图文

            List<Reply.News> news =reply.getNews();

            sb.append("<ArticleCount>").append(news.size()).append("</ArticleCount>");
            sb.append("<Articles>");

            for (Reply.News a : news) {
                sb.append("<item>");
                sb.append(" <Title><![CDATA[").append(a.getTitle()).append("]]></Title> ");
                sb.append(" <Description><![CDATA[").append(a.getDescription()).append("]]></Description> ");
                sb.append(" <PicUrl><![CDATA[").append(a.getPicUrl()).append("]]></PicUrl> ");
                sb.append(" <Url><![CDATA[").append(a.getUrl()).append("]]></Url> ");
                sb.append("</item>");
            }
            sb.append(" </Articles>");

        }
        sb.append("</xml>");

        return sb.toString();

    }
    private String getXMLInfo(HttpServletRequest request){
        String XMLData=null;
        ServletInputStream inputStream=null;
        BufferedReader bufferedReader=null;
        try {
            inputStream=request.getInputStream();
            bufferedReader= IoUtil.getReader(inputStream, "UTF-8");
            String data;
            StringBuilder builder=new StringBuilder();
            while ((data=bufferedReader.readLine())!=null){
                builder.append(data);
            }
            XMLData=builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
                if (bufferedReader!=null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return XMLData;
    }

    private Event parseXML(String data){
        Event e=new Event();
        Document d = Jsoup.parse(data);

        String toUserName = d.getElementsByTag("ToUserName").text();
        String fromUserName = d.getElementsByTag("FromUserName").text();
        String createTime = d.getElementsByTag("CreateTime").text();
        String msgType = d.getElementsByTag("MsgType").text();

        e.setWxID(toUserName);
        e.setOpenID(fromUserName);
        Elements contentE = d.getElementsByTag("Content");
        if (contentE.size()>0){
            String content = contentE.text();

            e.setContent(new Event.Content(toUserName, fromUserName, createTime, msgType, content));
        }else {
            String event = d.getElementsByTag("Event").text();
            if ("subscribe".equals(event)){
                Elements eventKeyE = d.getElementsByTag("EventKey");
                //扫描带参场景二维码，进行关注
                if (eventKeyE.size() > 0) {

                    String eventKey = eventKeyE.text();
                    String ticket = d.getElementsByTag("Ticket").text();
                    e.setScanSubscribe(new Event.ScanSubscribe(toUserName, fromUserName, createTime, msgType, event, eventKey, ticket));

                }else { //普通服务号关注
                    e.setSubscribe(new Event.Subscribe(toUserName, fromUserName, createTime, msgType, event));
                }
            }else if ("unsubscribe".equals(event)) {
                //取消关注
                e.setUnsubscribe(new Event.Unsubscribe(toUserName, fromUserName, createTime, msgType, event));

            }else if ("SCAN".equals(event)) {
                //关注的前提下扫描带参场景二维码

                String eventKey = d.getElementsByTag("EventKey").text();
                String ticket = d.getElementsByTag("Ticket").text();

                e.setScan(new Event.Scan(toUserName, fromUserName, createTime, msgType, event, eventKey, ticket));

            }else if ("LOCATION".equals(event)) {
                //上报地理位置

                String latitude = d.getElementsByTag("Latitude").text();
                String longitude = d.getElementsByTag("Longitude").text();
                String precision = d.getElementsByTag("Precision").text();

                e.setLocation(new Event.Location(toUserName, fromUserName, createTime, msgType, event, latitude, longitude, precision));

            }else if ("CLICK".equals(event)) {
                //点击菜单拉取消息====用户点击自定义菜单后(注意：点击菜单弹出子菜单，不会产生上报)

                String eventKey = d.getElementsByTag("EventKey").text();

                e.setClick(new Event.Click(toUserName, fromUserName, createTime, msgType, event, eventKey));

            }else if ("VIEW".equals(event)) {
                //点击菜单跳转链接====用户点击自定义菜单后(注意：点击菜单弹出子菜单，不会产生上报)

                String eventKey = d.getElementsByTag("EventKey").text();

                e.setView(new Event.View(toUserName, fromUserName, createTime, msgType, event, eventKey));
            }
        }
        return e;
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
            log.error((String) object.get("errmsg"));
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
