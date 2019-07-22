package com.xyzla.ws.h5.controller;

import com.xyzla.ws.h5.handler.MyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
@EnableScheduling
public class TaskSchedule {


    @Autowired
    MyHandler handler;

    @Scheduled(fixedDelay = 5000)
    public void sendMessage() {
        Boolean b = handler.sendMessageToUser("888", new TextMessage("服务器主动推送了一条消息"));
        System.out.println(b);
    }
}