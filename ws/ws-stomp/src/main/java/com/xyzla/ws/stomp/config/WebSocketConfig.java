package com.xyzla.ws.stomp.config;

import com.xyzla.ws.stomp.domain.Authentication;
import com.xyzla.ws.stomp.domain.Users;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Map;

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
         * 广播订阅(topic) 单独聊天(/user)
         */
        config.enableSimpleBroker("/topic", "/user");

        /*
         * 客户端发送过来的消息，需要以"/app"为前缀，再经过Broker转发给响应的Controller
         */
        config.setApplicationDestinationPrefixes("/app");

        /*
         * 一对一发送的前缀
         * 订阅主题：/user/{userID}/v3/greeting
         * 推送方式：1、@SendToUser("/v3/greeting")
         *          2、messagingTemplate.convertAndSendToUser(destUsername, "/demo3/greetings", greeting);
         */
        config.setUserDestinationPrefix("/user");
    }


    /**
     * 1. 为 configureClientInboundChannel 设置拦截器
     * 2. WebSocket 首次请求连接的时候，获取其 Header 信息，利用Header 里面的信息进行权限认证
     * 3. 通过认证的用户，使用 accessor.setUser(user); 方法，将登陆信息绑定在该 StompHeaderAccessor 上，在Controller方法上可以获取 StompHeaderAccessor 的相关信息
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //1. 判断是否首次连接请求
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    //2. 验证是否登录
                    String username = accessor.getNativeHeader("username").get(0);
                    String password = accessor.getNativeHeader("password").get(0);
                    for (Map.Entry<String, String> entry : Users.USERS_MAP.entrySet()) {
                        System.out.println(entry.getKey() + "---" + entry.getValue());
                        if (entry.getKey().equals(username) && entry.getValue().equals(password)) {
                            //验证成功,登录
                            Authentication user = new Authentication(username); // access authentication header(s)}
                            accessor.setUser(user);
                            return message;
                        }
                    }
                    return null;
                }
                //不是首次连接，已经成功登陆
                return message;
            }
        });
    }
}