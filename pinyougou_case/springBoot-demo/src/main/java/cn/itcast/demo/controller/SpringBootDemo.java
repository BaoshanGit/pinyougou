package cn.itcast.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringBootDemo {


    @Autowired
    private Environment evn;
    @RequestMapping("/spring")
    public String  springBoot(){
        return "Hello Spring Boot !!!" + evn.getProperty("url") ;
    }

    @RequestMapping("/spring1")
    public String  springBoot1(){
        return "Hello Spring Boot 111 !!!" + evn.getProperty("url") ;
    }
}
