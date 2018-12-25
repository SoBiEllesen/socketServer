package com.ixtens.server.tcp;

import com.ixtens.dto.RequestDto;
import com.ixtens.dto.ResponseDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TcpConnection {

    private static Log logger = LogFactory.getLog(TcpConnection.class);
    private final TcpRequestProcessor tcpRequestProcessor;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;

    public TcpConnection(Socket socket, TcpRequestProcessor tcpRequestProcessor) {
        this.socket = socket;
        this.tcpRequestProcessor = tcpRequestProcessor;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public synchronized void send(ResponseDto responseDto) {
        try {
            logger.info("send response " + responseDto.toString());
            objectOutputStream.writeObject(responseDto);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void start() {
        new Thread(() -> {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    Object o = objectInputStream.readObject();
                    tcpRequestProcessor.messageReceived(this, (RequestDto) o);
                } catch (EOFException | SocketException e) {
                    logger.error(e);
                    close();
                } catch (IOException | ClassNotFoundException ignore) {
                }
            }
        }).start();
    }

    public void close() {
        try {
            logger.info("Start close tcpConnection");
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
            logger.info("Tcp connection close successful");
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
