package com.ixtens.server.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TcpServerStarterApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${port}")
    private Integer port;

    private final TcpServer server;

    @Autowired
    public TcpServerStarterApplicationListener(TcpServer server) {
        this.server = server;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        server.setPort(port);
        server.start();
    }
}
