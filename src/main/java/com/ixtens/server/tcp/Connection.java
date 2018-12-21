package com.ixtens.server.tcp;

import com.ixtens.dto.RequestDto;
import com.ixtens.dto.ResponseDto;

import java.net.InetAddress;
import java.util.List;

public interface Connection {

    InetAddress getAddress();

    void send(ResponseDto responseDto);

    void addListeners(List<Listener> listener);

    void start();

    void close();

    interface Listener {

        void messageReceived(Connection connection, RequestDto requestDto);

        void connected(Connection connection);

        void disconnected(Connection connection);
    }
}
