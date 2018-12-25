package com.ixtens.server.tcp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Service
public class TcpServer {

    private static Log logger = LogFactory.getLog(TcpServer.class);

    private ServerSocket serverSocket;
    private final TcpRequestProcessor tcpRequestProcessor;

    @Autowired
    public TcpServer(TcpRequestProcessor tcpRequestProcessor) {
        this.tcpRequestProcessor = tcpRequestProcessor;
    }

    public void setPort(Integer port) {
        try {
            if (port == null) {
                logger.info("Property tcp.server.port not found. Use default port 1234");
                port = 1234;
            }
            serverSocket = new ServerSocket(port);
            logger.info("Server start at port " + port);
        } catch (IOException e) {
            logger.error("May be port " + port + " busy." + e);
        }
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    logger.info("trying to get new socket");
                    Socket socket = serverSocket.accept();
                    if (socket != null && socket.isConnected()) {
                        logger.info("open new socket");
                        TcpConnection tcpConnection = new TcpConnection(socket, tcpRequestProcessor);
                        tcpConnection.start();
                    }
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }).start();
    }
}
