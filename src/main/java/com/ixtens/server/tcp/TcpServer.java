package com.ixtens.server.tcp;

import com.ixtens.dto.RequestDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
public class TcpServer implements Connection.Listener {

    private static Log logger = LogFactory.getLog(TcpServer.class);

    private ServerSocket serverSocket;
    private List<Connection> connections = new ArrayList<>();
    private final List<Connection.Listener> listeners;

    @Autowired
    public TcpServer(List<Connection.Listener> listeners) {
        this.listeners = listeners;
        this.listeners.add(this);
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
                        TcpConnection tcpConnection = new TcpConnection(socket);
                        tcpConnection.start();
                        tcpConnection.addListeners(listeners);
                        connected(tcpConnection);
                    }
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }).start();
    }

    @Override
    public void messageReceived(Connection connection, RequestDto requestDto) {
        logger.info("Received new message from " + connection.getAddress().getCanonicalHostName());
        logger.info("Class name: " + requestDto.getClass().getCanonicalName() + ", toString: " + requestDto.toString());
    }

    @Override
    public void connected(Connection connection) {
        logger.trace("New connection! Ip: " + connection.getAddress().getCanonicalHostName() + ".");
        connections.add(connection);
        logger.trace("Current connections count: " + connections.size());
    }

    @Override
    public void disconnected(Connection connection) {
        logger.trace("Disconnect! Ip: " + connection.getAddress().getCanonicalHostName() + ".");
        connections.remove(connection);
        logger.trace("Current connections count: " + connections.size());
    }
}
