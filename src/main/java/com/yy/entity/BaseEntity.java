package com.yy.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Package: com.yy.entity
 * @ClassName: BaseEntity
 * @Author: Created By Yy
 * @Date: 2019-05-31 09:50
 */
@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP")
    @Transient
    protected Date createTime;

    @Column(columnDefinition = "timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Transient
    protected Date updateTime;
}
