package com.yy.common.config;

import com.alibaba.fastjson.JSON;
import com.yy.common.annotation.NoAspectj;
import com.yy.common.bean.Result;
import com.yy.common.enums.ReturnInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Package: com.yy.common.config
 * @ClassName: GlobalAspectj
 * @Author: Created By Yy
 * @Date: 2019-05-29 15:06
 */
@Component
@Aspect
@Slf4j
public class GlobalAspectj {
    @Pointcut("execution(* com.yy.controller.*.*(..))")
    public void controllerAspect(){

    }

    @Around("controllerAspect()")
    public Object proceed(ProceedingJoinPoint joinPoint) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("Request Method: "+ joinPoint.getSignature().getDeclaringTypeName()+"______"+joinPoint.getSignature().getName());
        Enumeration<String> names = request.getParameterNames();
        List<String> params=new ArrayList<>();
        while (names.hasMoreElements()){
            String name=names.nextElement();
            params.add(name+": "+request.getParameter(name));
        }
        log.info("Request Args: "+ JSON.toJSONString(params));
        String methodName = joinPoint.getSignature().getName();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, methodSignature.getParameterTypes());
        if (method.isAnnotationPresent(NoAspectj.class)){
            Object proceed = joinPoint.proceed();
            return proceed;
        }
        Result result=(Result)joinPoint.proceed();
        result.setCode(ReturnInfo.Success.getCode());
        result.setMsg(ReturnInfo.Success.getMsg());
        return result;
    }
}
