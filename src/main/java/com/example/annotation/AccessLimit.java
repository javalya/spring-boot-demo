package com.example.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * 防刷注解
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    int seconds() default 4;
    int maxCount() default 5;
    boolean needLogin()default true;
}
