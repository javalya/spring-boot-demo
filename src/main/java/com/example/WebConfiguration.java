package com.example;

import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Configuration
public class WebConfiguration {
    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }

    public class MyFilter implements Filter{
        @Override
        public void init(FilterConfig arg0) throws ServletException {
            System.out.println("过滤器生命开始");
            String filterName = arg0.getFilterName();
            System.out.println("过滤器名为 " + filterName);
            // 遍历过滤器中的参数和对应值
            Enumeration<String> initParas = arg0.getInitParameterNames();
            String paraName;
            String paraValue;
            while (initParas.hasMoreElements()) {
                paraName = initParas.nextElement();
                paraValue = arg0.getInitParameter(paraName);
                System.out.println(paraName + " = " + paraValue);
            }


        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {

            HttpServletRequest request = (HttpServletRequest) servletRequest;
            System.out.println("this is MyFilter,url :"+request.getRequestURI());
            filterChain.doFilter(servletRequest, servletResponse);
        }

        @Override
        public void destroy() {
            System.out.println("过滤器生命终止");
        }

    }
}
