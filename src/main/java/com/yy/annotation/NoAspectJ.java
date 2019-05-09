package com.yy.annotation;

import java.lang.annotation.*;

/**
 * @Package: com.yy.annotation
 * @ClassName: NoAspectJ
 * @Author: Created By Yy
 * @Date: 2019-05-09 17:07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface NoAspectJ {

}
