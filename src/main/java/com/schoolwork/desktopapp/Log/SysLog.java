package com.schoolwork.desktopapp.Log;


import java.lang.annotation.*;

/*
* 使用注解形式对日志进行记录
* */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}