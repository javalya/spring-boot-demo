package com.example.service;

import com.example.annotation.DemoAnnotation2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnnotationService {

    @DemoAnnotation2(arg1 = "a")
    public List<String> aspectTest(String a, String b, String c){
        List<String> l = new ArrayList<>();;
        l.add(a);
        l.add(b);
        l.add(c);
        return l;
    }
}
