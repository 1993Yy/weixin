package com.yy.common.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.yy.common.bean.Event;
import com.yy.common.bean.Reply;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * @Package: com.yy.common.util
 * @ClassName: XMLUtil
 * @Author: Created By Yy
 * @Date: 2019-05-30 15:25
 */
public class XMLUtil {
    /**
     * 获取请求信息
     * @param request
     * @return
     */
    public static String getXMLInfo(HttpServletRequest request){
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

    /**
     * 解析xml
     * @param xml
     * @param ip
     * @return
     */
    public static Event parseXML(String xml, String ip){
        Event e=new Event();
        Document d = Jsoup.parse(xml);

        String wxid = d.getElementsByTag("ToUserName").text();
        String userOpenid = d.getElementsByTag("FromUserName").text();
        long createTime = Long.valueOf(d.getElementsByTag("CreateTime").text());
        String msgType = d.getElementsByTag("MsgType").text();

        e.setWxid(wxid).setUserOpenid(userOpenid).setUserIP(ip).setMsgType(msgType);
        //事件
        String event = d.getElementsByTag("Event").text();
        if (StrUtil.isNotEmpty(event)){
            //关注
            if ("subscribe".equals(event)){
                Elements elements = d.getElementsByTag("Ticket");
                //扫码关注
                if (elements.size()>0){
                    String eventKey = d.getElementsByTag("EventKey").text();
                    String ticket = d.getElementsByTag("Ticket").text();
                    e.setScanSubscribe(new Event.Scansubscribe(wxid,userOpenid,createTime,msgType,event,eventKey,ticket));
                    //普通关注
                }else {
                    e.setSubscribe(new Event.Subscribe(wxid,userOpenid,createTime,msgType,event));
                }
                //取消关注
            }else if ("unsubscribe".equals(event)){
                e.setUnsubscribe(new Event.Unsubscribe(wxid,userOpenid,createTime,msgType,event));
                //已关注 扫码
            }else if ("SCAN".equals(event)){
                String eventKey = d.getElementsByTag("EventKey").text();
                String ticket = d.getElementsByTag("Ticket").text();
                e.setScan(new Event.Scan(wxid,userOpenid,createTime,msgType,event,eventKey,ticket));
                //上报地理位置
            }else if ("LOCATION".equals(event)){
                String latitude = d.getElementsByTag("Latitude").text();
                String longitude = d.getElementsByTag("Longitude").text();
                String precision = d.getElementsByTag("Precision").text();
                e.setLocation(new Event.Location(wxid,userOpenid,createTime,msgType,event,latitude,longitude,precision));
                //菜单点击
            }else if ("CLICK".equals(event)){
                String eventKey = d.getElementsByTag("EventKey").text();
                e.setClick(new Event.Click(wxid,userOpenid,createTime,msgType,event,eventKey));
            }
        }
        //t普通消息
        if ("text".equals(msgType)){
            String content = d.getElementsByTag("Content").text();
            String msgId = d.getElementsByTag("MsgId").text();
            e.setText(new Event.Text(wxid,userOpenid,createTime,msgType,content,msgId));
        }else if ("image".equals(msgType)){
            String picUrl = d.getElementsByTag("PicUrl").text();
            String mediaId = d.getElementsByTag("MediaId").text();
            String msgId = d.getElementsByTag("MsgId").text();
            e.setImage(new Event.Image(wxid,userOpenid,createTime,msgType,picUrl,mediaId,msgId));
        }else if ("voice".equals(msgType)){
            String mediaId = d.getElementsByTag("MediaId").text();
            String format = d.getElementsByTag("Format").text();
            String msgID = d.getElementsByTag("MsgID").text();
            e.setVoice(new Event.Voice(wxid,userOpenid,createTime,msgType,format,mediaId,msgID));
        }
        return e;
    }

    /**
     * 组装回复消息
     * @param event
     * @param reply
     * @return
     */
    public static String getResponseXML(Event event, Reply.MsgType msgType,Reply reply){
        StringBuilder builder=new StringBuilder();
        builder.append("<xml>");
        builder.append("<ToUserName><![CDATA[").append(event.getUserOpenid()).append("]]></ToUserName>");
        builder.append("<FromUserName><![CDATA[").append(event.getWxid()).append("]]></FromUserName>");
        builder.append("<CreateTime>").append(System.currentTimeMillis()/1000).append("</CreateTime>");
        builder.append("<MsgType><![CDATA[").append(msgType).append("]]></MsgType>");
        //文本
        if (Reply.MsgType.text.equals(msgType)){
            builder.append("<Content><![CDATA[").append(reply.getContent().getContent()).append("]]></Content>");
        //图片
        }else if (Reply.MsgType.image.equals(msgType)){
            builder.append("<Image><MediaId><![CDATA[").append(reply.getImage().getMediaId()).append("]]></MediaId></Image>");
        //语音
        }else if (Reply.MsgType.voice.equals(msgType)){
            builder.append("<Voice><MediaId><![CDATA[").append(reply.getVoice().getMediaId()).append("]]></MediaId></Voice>");
        //视频
        }else if (Reply.MsgType.video.equals(msgType)){
            builder.append(" <Video>");
            builder.append("<MediaId><![CDATA[").append(reply.getVideo().getMediaId()).append("]]></MediaId>");
            builder.append("<Title><![CDATA[").append(reply.getVideo().getTitle()).append("]]></Title>");
            builder.append("<Description><![CDATA[").append(reply.getVideo().getDescription()).append("]]></Description>");
            builder.append(" </Video>");
        //音乐
        }else if (Reply.MsgType.music.equals(msgType)){
            builder.append("<Music>");
            builder.append("<Title><![CDATA[").append(reply.getMusic().getTitle()).append("]]></Title>");
            builder.append("<Description><![").append(reply.getMusic().getDescription()).append("]]></Description>");
            builder.append("<MusicUrl><![").append(reply.getMusic().getMusicURL()).append("]]></MusicUrl>");
            builder.append("<HQMusicUrl><![CDATA[").append(reply.getMusic().getHQMusicUrl()).append("]]></HQMusicUrl>");
            builder.append("<ThumbMediaId><![CDATA[").append(reply.getMusic().getThumbMediaId()).append("]]></ThumbMediaId>");
            builder.append(" <Music>");
        //图文
        }else if (Reply.MsgType.news.equals(msgType)){
            builder.append(" <ArticleCount>").append(reply.getNews().size()).append("</ArticleCount>");
            builder.append(" <Articles>");
            List<Reply.News> news = reply.getNews();
            news.forEach(a->{
                builder.append(" <item>");
                builder.append("<Title><![CDATA[").append(a.getTitle()).append("]]></Title>");
                builder.append("<Description><![CDATA[").append(a.getDescription()).append("]]></Description>");
                builder.append("<PicUrl><![CDATA[").append(a.getPicUrl()).append("]]></PicUrl>");
                builder.append("<Url><![CDATA[").append(a.getUrl()).append("]]></Url>");
                builder.append("</item>");
            });
            builder.append(" </Articles>");
        }
        builder.append("</xml>");
        return builder.toString();
    }
}
