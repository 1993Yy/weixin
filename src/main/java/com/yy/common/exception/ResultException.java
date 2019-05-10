package com.yy.common.exception;

import lombok.Getter;

/**
 * @Package: com.yy.common.exception
 * @ClassName: ResultException
 * @Author: Created By Yy
 * @Date: 2019-05-09 14:11
 */
@Getter
public class ResultException extends RuntimeException{

    private String code;

    private String msg;

    public ResultException(String code,String msg){
        super();
        this.code=code;
        this.msg=msg;
    }
}
