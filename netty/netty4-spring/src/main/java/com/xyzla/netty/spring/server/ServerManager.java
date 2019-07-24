package com.xyzla.netty.spring.server;

import com.xyzla.netty.spring.context.AppContext;
import org.springframework.stereotype.Component;

@Component
public class ServerManager {
    private AbstractNettyServer tcpServer;

    public ServerManager() {
        tcpServer = (AbstractNettyServer) AppContext.getBean(AppContext.TCP_SERVER);
    }

    public void startServer(int port) throws Exception {
        tcpServer.startServer(port);
    }

    public void startServer() throws Exception {
        tcpServer.startServer();
    }

    public void stopServer() throws Exception {
        tcpServer.stopServer();
    }
}
