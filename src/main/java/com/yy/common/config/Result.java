package com.yy.common.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Package: com.yy.common.config
 * @ClassName: Result
 * @Author: Created By Yy
 * @Date: 2019-05-09 14:36
 */
@Data
@NoArgsConstructor
public class Result<T> {

    private String code;

    private String msg;

    private T data;

    public Result(T data){
        this.data=data;
    }

    public Result(String code,String msg,T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

}
