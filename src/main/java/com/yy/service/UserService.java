package com.yy.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yy.dao.UserDao;
import com.yy.entity.QUser;
import com.yy.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * @Package: com.yy.service
 * @ClassName: UserService
 * @Author: Created By Yy
 * @Date: 2019-05-10 10:13
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory query;
    @PostConstruct
    public void init(){
        query=new JPAQueryFactory(entityManager);
    }
    /**
     * 增加用户
     * @param data
     * @return
     */
    @Transactional
    public long addUser(Map<String,String> data){
        QUser qUser=QUser.user;
        User u = userDao.findByOpenID(data.get("openid"));
        if (u ==null){
            User user=new User();
            user.setSubscribe(Integer.valueOf(data.get("subscribe"))).setOpenID(data.get("openid")).setNickName(data.get("nickname"))
                    .setSex(Integer.valueOf(data.get("sex"))).setLanguage(data.get("language")).setCity(data.get("city"))
                    .setProvince(data.get("province")).setCountry(data.get("country")).setHeadImgUrl(data.get("headimgurl"))
                    .setSubscribeTime(Long.valueOf(data.get("subscribe_time"))).setUnionID(data.get("unionid"))
                    .setRemark(data.get("remark")).setGroupID(data.get("groupid")).setSubscribeScene(data.get("subscribe_scene"))
                    .setQrScene(data.get("qr_scene")).setQrSceneStr(data.get("qr_scene_str"));
            User save = userDao.save(user);
            return save.getId();
        }else {
            System.out.println("====");
            long execute = query.update(qUser).where(qUser.openID.eq(data.get("openid"))).set(qUser.subscribe, Integer.valueOf(data.get("subscribe")))
                    .set(qUser.nickName, data.get("nickname")).set(qUser.headImgUrl, data.get("headimgurl"))
                    .set(qUser.sex, Integer.valueOf(data.get("sex"))).set(qUser.city, data.get("city"))
                    .set(qUser.province, data.get("province")).set(qUser.country, data.get("country")).execute();
            System.out.println(execute);
            return execute;
        }
    }

    /**
     * 获取微信logo图片
     * @return
     */
    public String getHeadImg(){
        QUser qUser=QUser.user;
        JPAQuery<String> str = query.select(qUser.headImgUrl).from(qUser).where(qUser.role.eq("1"));
        return str.fetchOne();
    }
}
