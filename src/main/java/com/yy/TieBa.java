package com.yy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.yy.common.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.yy.spider
 * @ClassName: TieBa
 * @Author: Created By Yy
 * @Date: 2019-08-23 14:21
 */
@Slf4j
public class TieBa {

    public static void main(String[] args) throws Exception {
        OkHttpUtil okHttpUtil=new OkHttpUtil();
        Connection connection = getConnection();
        int count=0;
        List<Map<String,Object>> li=new ArrayList<>();
        while (true){
            log.info("开始采集："+(count+1));
            String url="http://tieba.baidu.com/f?kw=摄影&ie=utf-8&pn="+(count*50);
            String data = okHttpUtil.doGetString(url);
            JXDocument document = JXDocument.create(data);
            String xpath="//*[@id=\"thread_list\"]/li[position()>1]";
            List<JXNode> nodes = document.selN(xpath);
            String replyPath="/div/div/span/text()";
            String titlePath="/div/div[2]/div/div/a/text()";
            String authorPath="/div/div[2]/div/div[2]//a/text()";
            String createPath="/div/div[2]/div/div[2]/span[2]/text()";
            String urlPath="/div/div[2]/div[2]//img/@bpic";
            nodes.forEach(a->{
                JXNode replyNode = a.selOne(replyPath);
                JXNode titleNode = a.selOne(titlePath);
                JXNode authorNode = a.selOne(authorPath);
                JXNode createNode = a.selOne(createPath);
                List<JXNode> urlNode = a.sel(urlPath);
                if (CollUtil.isEmpty(urlNode)){
                    return;
                }
                List<String> list=new ArrayList<>();
                urlNode.forEach(b->{
                    list.add(b.toString());
                });
                Map<String,Object> map=new HashMap<>(5);
                map.put("title",titleNode.toString());
                map.put("createTime",createNode.toString());
                map.put("author",authorNode.toString());
                map.put("reply",replyNode.toString());
                map.put("url",list);
                li.add(map);
            });
            count++;
            log.info("开始采集："+(count));
            TimeUnit.SECONDS.sleep(4);
            if (count==10000){
                break;
            }

            log.info("开始保存");
            addData(okHttpUtil,connection,li);
            li.clear();
            log.info("结束保存");
        }
    }

    private static void addData(OkHttpUtil okHttpUtil,Connection conn,List<Map<String, Object>> list) {
        PreparedStatement statement;
        String sql="insert into tieba(title,create_time,author,reply,url) values(?,?,?,?,?)";
        for (Map<String, Object> map : list) {
            try {
                List<String> url = (List<String>)map.get("url");
                Iterator<String> iterator = url.iterator();
                while (iterator.hasNext()){
                    String next = iterator.next();
                    if (StrUtil.isEmpty(next)){
                        iterator.remove();
                    }
                }
                if (CollUtil.isEmpty(url)){
                    return;
                }
                statement=conn.prepareStatement(sql);
                statement.setString(1,map.get("title")+"");
                statement.setString(2,map.get("createTime")+"");
                statement.setString(3,map.get("author")+"");
                statement.setString(4,map.get("reply")+"");
                statement.setString(5,map.get("url").toString());
                int i = statement.executeUpdate();

                url.forEach(a->{
                    String decode = null;
                    try {
                        decode = URLDecoder.decode(a, "UTF-8");
                        InputStream inputStream = okHttpUtil.doGetStream(decode);
                        IoUtil.copy(inputStream,new FileOutputStream("C:\\Users\\Yy\\Desktop\\img\\"+decode.substring(decode.length()-32)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static Connection getConnection(){
        Connection conn=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/Yy?serverTimezone=Asia/Shanghai";
            String userName="root";
            String passWord="root";
            conn = DriverManager.getConnection(url, userName, passWord);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return conn;
    }

}
