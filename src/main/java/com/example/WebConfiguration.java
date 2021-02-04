package com.example;

import com.example.interceptor.AccessLimitInterceptor;
import org.apache.catalina.filters.RemoteIpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    Logger log = LoggerFactory.getLogger(WebConfiguration.class);
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

    @Autowired
    private AccessLimitInterceptor interceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }

    public class MyFilter implements Filter{
        @Override
        public void init(FilterConfig arg0) throws ServletException {
            log.info("过滤器生命开始");
            String filterName = arg0.getFilterName();
            log.info("过滤器名为 " + filterName);
            // 遍历过滤器中的参数和对应值
            Enumeration<String> initParas = arg0.getInitParameterNames();
            String paraName;
            String paraValue;
            while (initParas.hasMoreElements()) {
                paraName = initParas.nextElement();
                paraValue = arg0.getInitParameter(paraName);
                log.info(paraName + " = " + paraValue);
            }


        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {

            HttpServletRequest request = (HttpServletRequest) servletRequest;
            log.info("this is MyFilter,url :"+request.getRequestURI());
            filterChain.doFilter(servletRequest, servletResponse);
        }

        @Override
        public void destroy() {
            log.info("过滤器生命终止");
        }

    }
}
