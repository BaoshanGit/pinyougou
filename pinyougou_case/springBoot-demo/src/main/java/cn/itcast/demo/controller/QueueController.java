package cn.itcast.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class QueueController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @RequestMapping("/sendMap")
    public void send(){
        Map map = new HashMap();
        map.put("mobile",123456789);
        map.put("name","张三");
        jmsMessagingTemplate.convertAndSend("ceshi",map);
    }
}
