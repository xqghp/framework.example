package com.xyzla.ws.stomp.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Task {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;//消息发送模板

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;//消息发送模板


    @Scheduled(fixedRate = 10000)
    public void sendMessage() {
        System.out.println("定时任务");
        this.simpMessagingTemplate.convertAndSend("/topic/greeting", "写在@Component @EnableScheduling 中的定时任务");
    }
}
