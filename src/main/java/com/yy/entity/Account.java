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
 * @ClassName: Account
 * @Author: Created By Yy
 * @Date: 2019-05-31 09:35
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name = "app_account")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Account extends BaseEntity{

    private String appID;

    private String appSecret;

    private String token;

    private String accessToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime;
}
