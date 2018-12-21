package com.ixtens.server.tcp;

import com.ixtens.dto.RequestDto;
import com.ixtens.dto.ResponseDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class TcpConnection implements Connection {

    private static Log logger = LogFactory.getLog(TcpConnection.class);

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Socket socket;
    private List<Listener> listeners = new ArrayList<>();

    public TcpConnection(Socket socket) {
        this.socket = socket;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InetAddress getAddress() {
        return socket.getInetAddress();
    }

    @Override
    public synchronized void send(ResponseDto responseDto) {
        try {
            logger.info("send response " + responseDto.toString());
            objectOutputStream.writeObject(responseDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addListeners(List<Listener> listener) {
        listeners.addAll(listener);
    }

    @Override
    public void start() {
        new Thread(() -> {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    Object o = objectInputStream.readObject();
                    for (Listener listener : listeners) {
                        listener.messageReceived(this, (RequestDto) o);
                    }
                } catch (EOFException | SocketException e) {
                    logger.error(e);
                    for (Listener listener : listeners) {
                        listener.disconnected(this);
                    }
                    close();
                } catch (IOException | ClassNotFoundException ignore) {
                    logger.error(ignore);
                }
            }
        }).start();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    @Override
    public void close() {
        try {
            logger.info("IN CLOSE CONNECTION");
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
