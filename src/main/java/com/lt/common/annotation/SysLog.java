package com.lt.common.annotation;

import java.lang.annotation.*;

/**
 * @description: 系统日志注解
 * @author: ~Teng~
 * @date: 2022/11/17 15:17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
