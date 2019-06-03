package com.yy.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yy.dao.UserDao;
import com.yy.entity.QUser;
import com.yy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * @Package: com.yy.service
 * @ClassName: UserService
 * @Author: Created By Yy
 * @Date: 2019-05-31 09:39
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EntityManager entityManager;

    private JPAQueryFactory query;

    @PostConstruct
    public void setQuery(){
        query=new JPAQueryFactory(entityManager);
    }

    /**
     * 更新保存用户
     */
    @Transactional
    public long addOrUpdateUser(String userInfo){
        Map data=(Map) JSONObject.parse(userInfo);
        String openid = data.get("openid").toString();
        boolean existUser = isExistUser(openid);
        QUser user = QUser.user;
        int subscribe = (int) data.get("subscribe");
        long result=0;
        if (existUser){
            if (subscribe==1){
                result =query.update(user)
                        .set(user.nickName,(String) data.get("nickname"))
                        .set(user.sex,(int)data.get("sex"))
                        .set(user.city,(String)data.get("city"))
                        .set(user.province,(String)data.get("province"))
                        .set(user.country,(String)data.get("country"))
                        .set(user.remark,(String)data.get("remark"))
                        .set(user.groupid,(Integer) data.get("groupid"))
                        .set(user.tagidList,data.get("tagid_list").toString())
                        .set(user.subscribeScene,(String)data.get("subscribe_scene"))
                        .set(user.qrScene,(int)data.get("qr_scene"))
                        .set(user.subscribeTime,DateUtil.date(Long.valueOf(String.valueOf(data.get("subscribe_time")))*1000))
                        .set(user.qrSceneStr,(String)data.get("qr_scene_str"))
                        .execute();
            }else {
                result= query.update(user).set(user.subscribe,0).execute();
            }
        }else {
            if (subscribe==1){
                User u = JSONObject.parseObject(userInfo, User.class);
                u.setSubscribeTime(DateUtil.date(u.getSubscribeTime().getTime()*1000));
                result= userDao.save(u).getId();
            }
        }
        return result;
    }

    /**
     * 获取logo
     * @return
     */
    public String getLogo(){
        QUser user = QUser.user;
        JPAQuery<String> data = query.select(user.headimgurl)
                .from(user)
                .where(user.id.eq(1));
        return data.fetchOne();
    }

    /**
     * 是否存在用户
     * @return
     */
    private boolean isExistUser(String openid){
        QUser user = QUser.user;
        return userDao.exists(user.openID.eq(openid));
    }

}
