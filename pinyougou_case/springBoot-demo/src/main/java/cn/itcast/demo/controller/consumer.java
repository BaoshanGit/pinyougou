package cn.itcast.demo.controller;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class consumer {

    @JmsListener(destination = "ceshi")
    public void readMessage(Map map){
        System.out.println("接收到的消息："+map);
    }
}
