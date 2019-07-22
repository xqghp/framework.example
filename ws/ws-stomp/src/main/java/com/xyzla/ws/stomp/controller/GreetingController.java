package com.xyzla.ws.stomp.controller;

import com.xyzla.ws.stomp.domain.Authentication;
import com.xyzla.ws.stomp.message.Greeting;
import com.xyzla.ws.stomp.message.GreetingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.Map;

@Controller
public class GreetingController {

    private static Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;//消息发送模板

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;//消息发送模板


    @RequestMapping(value = {"/greeting1"}, method = RequestMethod.GET)
    public void greetingV1() {

    }

    /*
     * 使用restful风格
     */
    @MessageMapping("/v1/greeting/{typeId}")
    @SendTo("/topic/v1/greeting")
    public Greeting greetingV1$1(@DestinationVariable Integer typeId, GreetingMessage message, @Headers Map<String, Object> headers) throws Exception {
        return new Greeting(headers.get("simpSessionId").toString(), typeId + "---" + message.getMessage());
    }

    /*
     * 这里没用@SendTo注解指明消息目标接收者，消息将默认通过@SendTo("/topic/v1/greeting2/{typeId}")交给Broker进行处理
     * 不推荐不使用@SendTo注解指明目标接受者
     */
    @MessageMapping("/v1/greeting2/{typeId}")
    public Greeting greetingV1$2(GreetingMessage message) {
        return new Greeting("这是没有指明目标接受者的消息:", message.getMessage());
    }

    @RequestMapping(value = {"/greeting2"}, method = RequestMethod.GET)
    public void greetingV2() {

    }


    @MessageMapping("/v2/greeting/{typeId}")
    @SendTo("/topic/v2/greeting")
    public Greeting greetingV2(@DestinationVariable Integer typeId, GreetingMessage message, StompHeaderAccessor headerAccessor) {
        Authentication user = (Authentication) headerAccessor.getUser();
        String sessionId = headerAccessor.getSessionId();
        return new Greeting(user.getName(), typeId + "---" + "sessionId: " + sessionId + ", message: " + message.getMessage());
    }


    @RequestMapping(value = {"/greeting3_1"}, method = RequestMethod.GET)
    public void greetingV3_1() {

    }

    @RequestMapping(value = {"/greeting3_2"}, method = RequestMethod.GET)
    public void greetingV3_2() {

    }

    ///user/' + $('#username').val() + '/v3/greeting

    //https://xyzla.com/v3/greeting/13611212304
    @MessageMapping("/v3/greeting/{destUsername}")
    @SendToUser("/topic/v3/greeting")
    public Greeting greetingV3(@DestinationVariable String destUsername, GreetingMessage message, StompHeaderAccessor headerAccessor) throws Exception {
        Authentication user = (Authentication) headerAccessor.getUser();
        String sessionId = headerAccessor.getSessionId();
        Greeting greeting = new Greeting(user.getName(), "sessionId: " + sessionId + ", message: " + message.getMessage());
        /*
         * 对目标进行发送信息
         */
        simpMessagingTemplate.convertAndSendToUser(destUsername, "/v3/greeting", greeting);
        return new Greeting("系统", new Date().toString() + "消息已被推送。");
    }



    @Scheduled(fixedRate = 10000)
    public void sendMessage() {
        System.out.println("定时任务");
        this.simpMessagingTemplate.convertAndSend("/topic/v1/greeting", new Greeting("123", "写在@Controller中的定时任务"));
    }

}
