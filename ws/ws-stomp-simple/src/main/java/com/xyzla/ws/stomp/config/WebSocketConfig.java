package com.xyzla.ws.stomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//springBoot2.0版本后使用 实现WebSocketMessageBrokerConfigurer接口；
//2.0以下版本继承AbstractWebSocketMessageBrokerConfigurer 类；
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个Stomp 协议的endpoint指定URL为myWebSocket,并用.withSockJS()指定 SockJS协议。.setAllowedOrigins("*")设置跨域
        registry.addEndpoint("/socket").setAllowedOrigins("*").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        /*
         * 配置消息代理(message broker)
         * 用户可以订阅来自"/topic"和"/user"的消息，
         * 在Controller中，可通过@SendTo注解指明发送目标，这样服务器就可以将消息发送到订阅相关消息的客户端
         *
         * 在本Demo中，使用topic来达到群发效果，使用user进行一对一发送
         *
         * 客户端只可以订阅这两个前缀的主题
         * 广播订阅(topic) 单独聊天(/user)   群发（mass）  单独聊天（alone）
         */
        config.enableSimpleBroker("/topic", "/user", "/queue", "/mass", "/alone");

        /*
         * 客户端发送过来的消息，需要以"/app"为前缀，再经过Broker转发给响应的Controller
         */
        config.setApplicationDestinationPrefixes("/app");

        /*
         * 一对一发送的前缀
         * 订阅主题：/user/{userID}/demo3/greetings
         * 推送方式：1、@SendToUser("/demo3/greetings")
         *          2、messagingTemplate.convertAndSendToUser(destUsername, "/demo3/greetings", greeting);
         */
        config.setUserDestinationPrefix("/user");
    }

}