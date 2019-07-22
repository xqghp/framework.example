package com.xyzla.ws.stomp.controller;

import com.xyzla.ws.stomp.message.ChatRoomRequest;
import com.xyzla.ws.stomp.message.ChatRoomResponse;
import com.xyzla.ws.stomp.message.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class StompController {

    private static Logger logger = LoggerFactory.getLogger(StompController.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;//消息发送模板

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;//消息发送模板


    @RequestMapping("/stomp1")
    public void stomp1() {

    }

    @RequestMapping("/stomp2")
    public void stomp2() {

    }

    @RequestMapping("/stomp3")
    public void stomp3() {

    }


    //广播推送消息
    @Scheduled(fixedRate = 10000)
    public void sendTopicMessage() {
        User user = new User();
        user.setUserId(11);
        user.setUserName("Jack");
        user.setAge(10);
        this.simpMessagingTemplate.convertAndSend("/topic/getResponse", user);
    }

    //一对一推送消息
    @Scheduled(fixedRate = 10000)
    public void sendQueueMessage() {
        User user = new User();
        user.setUserId(1);
        user.setUserName("DaPeng");
        user.setAge(30);
        this.simpMessagingTemplate.convertAndSendToUser(user.getUserId() + "", "/queue/getResponse", user);
    }


    //群发
    @MessageMapping("/massRequest")
    //SendTo 发送至 Broker 下的指定订阅路径
    @SendTo("/mass/getResponse")
    public ChatRoomResponse mass(ChatRoomRequest chatRoomRequest) {
        //方法用于群发测试
        System.out.println("name = " + chatRoomRequest.getName());
        System.out.println("chatValue = " + chatRoomRequest.getChatValue());
        ChatRoomResponse response = new ChatRoomResponse();
        response.setName(chatRoomRequest.getName());
        response.setChatValue(chatRoomRequest.getChatValue());
        return response;
    }

    //单独聊天
    @MessageMapping("/aloneRequest")
    public ChatRoomResponse alone(ChatRoomRequest chatRoomRequest) {
        //方法用于一对一测试
        System.out.println("userId = " + chatRoomRequest.getUserId());
        System.out.println("name = " + chatRoomRequest.getName());
        System.out.println("chatValue = " + chatRoomRequest.getChatValue());
        ChatRoomResponse response = new ChatRoomResponse();
        response.setName(chatRoomRequest.getName());
        response.setChatValue(chatRoomRequest.getChatValue());
        this.simpMessagingTemplate.convertAndSendToUser(chatRoomRequest.getUserId() + "", "/alone/getResponse", response);
        return response;
    }

    //https://xyzla.com/msg/sendAll
    @ResponseBody
    @GetMapping(value = "/msg/sendAll")
    public User SendToCommUserMessage(HttpServletRequest request) {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        User user = new User();
        user.setUserId(1);
        user.setUserName("DaPeng");
        user.setAge(30);
        simpMessageSendingOperations.convertAndSend("/topic/getResponse", user);
        simpMessagingTemplate.convertAndSend("/topic/getResponse", user);
        return user;
    }

}
