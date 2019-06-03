package com.yy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @Package: com.yy.entity
 * @ClassName: User
 * @Author: Created By Yy
 * @Date: 2019-05-31 09:34
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "app_user")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends BaseEntity{

    private String nickName;

    private String openID;

    private Integer subscribe;

    private Integer sex;

    private String language;

    private String country;

    private String province;

    private String city;

    private String headimgurl;

    @Temporal(TemporalType.TIMESTAMP)
    private Date subscribeTime;

    private String unionid;

    private String remark;

    private Integer groupid;

    private String tagidList;

    private String subscribeScene;

    private Integer qrScene;

    private String qrSceneStr;

}
