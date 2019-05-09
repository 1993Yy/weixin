package com.yy.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * @Package: com.yy.entity
 * @ClassName: Account
 * @Author: Created By Yy
 * @Date: 2019-05-09 10:14
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "app_weixin_account")
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String appID;

    private String appSecret;

    private String token;

    private String accessToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
