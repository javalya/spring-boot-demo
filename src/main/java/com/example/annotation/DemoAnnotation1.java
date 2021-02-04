package com.example.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 测试注解
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface DemoAnnotation1 {

    String arg1() default "1";

    String arg2() default "2";


}
