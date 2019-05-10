package com.yy.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Package: com.yy.entity
 * @ClassName: User
 * @Author: Created By Yy
 * @Date: 2019-05-10 09:53
 */
@Data
@Accessors(chain =true)
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int subscribe;

    private String openID;

    private String nickName;

    private int sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String headImgUrl;

    private Long subscribeTime;

    private String unionID;

    private String remark;

    private String groupID;

    private String subscribeScene;

    private String qrScene;

    private String qrSceneStr;

    private String role="0";

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;
}
