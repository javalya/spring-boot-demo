package com.example.interceptor;

import com.example.annotation.AccessLimit;
import com.example.utils.RequestUtils;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    Logger log = LoggerFactory.getLogger(AccessLimitInterceptor.class);

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try{
            //判断请求是否属于方法的请求
            if(handler instanceof HandlerMethod){

                HandlerMethod hm = (HandlerMethod) handler;

                //获取方法中的注解,看是否有该注解
                AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
                if(accessLimit == null){
                    return true;
                }
                int seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                boolean login = accessLimit.needLogin();
                //根据 IP + API 限流
                String key = RequestUtils.getIpAddr(request) +"-"+ request.getRequestURI();

                //如果需要登录
                if(login){
                    //获取登录的session进行判断
                    String uid = (String) request.getSession().getAttribute("uid");
                    if(StringUtils.isNotEmpty(uid)){
                        key+=""+uid;
                    }

                }
                log.info("key: {}", key);
                //根据key获取已请求次数
                Integer count  = redisTemplate.opsForValue().get(key);
                log.info("count: {}", count);
                if(count  == null){
                    //set时一定要加过期时间
                    redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
                }else if(count < maxCount){
                    redisTemplate.opsForValue().set(key, count+1, seconds, TimeUnit.SECONDS);
                }else{
                    // 30405 API_REQUEST_TOO_MUCH  请求过于频繁
                    RequestUtils.out(response, "too many request ");
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            log.error("API请求限流拦截异常,请检查Redis是否开启!",e);
            throw new RuntimeException("API请求限流拦截异常,请检查Redis是否开启!");
        }
    }
}
