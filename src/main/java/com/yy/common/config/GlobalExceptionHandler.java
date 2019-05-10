package com.yy.common.config;

import com.yy.common.exception.ResultException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Package: com.yy.common.config
 * @ClassName: ExceptionHandler
 * @Author: Created By Yy
 * @Date: 2019-05-09 14:34
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResultException.class)
    public Result resultExceptionHandler(HttpServletRequest request,Exception ex){
        ResultException resultException=(ResultException)ex;
        Result result=new Result();
        result.setCode(resultException.getCode());
        result.setMsg(resultException.getMsg());
        return result;
    }
}
