package com.example.interceptor;


import com.example.annotation.DemoAnnotation2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * 测试注解切面类
 */
@Component
@Aspect
public class DemoAnnotationInterceptor {

    private Logger log = LoggerFactory.getLogger(DemoAnnotationInterceptor.class);

    @PostConstruct
    public void init() {
        log.info("DemoAnnotation start");
    }

    @Pointcut("@annotation(com.example.annotation.DemoAnnotation1)")
    public void DemoInterceptor1() {

    }

    @Pointcut("@annotation(com.example.annotation.DemoAnnotation2)")
    public void DemoInterceptor2() {

    }

    @AfterReturning(value = "DemoInterceptor1()")
    public void Interceptor1Common(JoinPoint point) {
        log.info("DemoInterceptor1");
        point.getArgs();
        log.info(point.toLongString());

    }

    @Before("DemoInterceptor2()")
    public void beforeMethod(JoinPoint joinPoint) {
        System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
        System.out.println("目标方法所属类的简单类名:" + joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("第" + (i + 1) + "个参数为:" + args[i]);
        }
        System.out.println("被代理的对象:" + joinPoint.getTarget());
        System.out.println("代理对象自己:" + joinPoint.getThis());
    }


    @AfterReturning(value = "DemoInterceptor2()", returning = "result")
    public void Interceptor2Common(JoinPoint joinPoint, Object result) {
        log.info("DemoInterceptor2");
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        log.info("args: {}", Arrays.toString(joinPoint.getArgs()));
        log.info("result: {}", result);
        String arg1 = method.getAnnotation(DemoAnnotation2.class).arg1();
        String arg2 = method.getAnnotation(DemoAnnotation2.class).arg2();


    }
}
