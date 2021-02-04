package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

public class RequestUtils {

    private static Logger log = LoggerFactory.getLogger(RequestUtils.class);
    /**
     * @Title: out
     * @Description:  response输出JSON数据
     * @param response : 响应请求
     * @param object: object
     * @return void
     **/
    public static void out(ServletResponse response, Object object){
        PrintWriter out = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(JSONObject.toJSONString(object));
        } catch (Exception e) {
            log.error("输出JSON报错!"+e);
        }finally{
            if(null != out){
                out.flush();
                out.close();
            }
        }
    }

    /**
     * IpUtils工具类方法
     * 获取真实的ip地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}
