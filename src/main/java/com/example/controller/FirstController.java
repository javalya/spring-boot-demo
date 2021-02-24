package com.example.controller;

import com.example.annotation.AccessLimit;
import com.example.annotation.DemoAnnotation1;
import com.example.annotation.DemoAnnotation2;
import com.example.domain.User;
import com.example.domain.UserDAO;
import com.example.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;


@RestController
public class FirstController {

    @Autowired
    private UserDAO userRepository;

    @Autowired
    private AnnotationService annotationService;

    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

    @RequestMapping("/getUser")
    @Cacheable(value="user-key")
    public List<User> getUser(String name) {
        List<User> user=userRepository.findByUserNameLike(name);
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
        return user;
    }

    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid.toString());
        return session.getId();
    }

    /**
     * 接口防刷测试
     * @return
     */
    @AccessLimit(seconds=20, maxCount=3)
    @ResponseBody
    @RequestMapping("/fangshua")
    public String fangshua(){
        return "请求成功";
    }

    /**
     * 注解测试
     * @return
     */

    @DemoAnnotation1
    @ResponseBody
    @RequestMapping("/annotation1")
    public String demoAnnotation1(){
        return "请求成功1";
    }

    @ResponseBody
    @RequestMapping("/annotation2")
    public List<String> demoAnnotation2(String a,String b,String c){
        return annotationService.aspectTest(a, b, c);
    }



}
