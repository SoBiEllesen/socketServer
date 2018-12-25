package com.ixtens.server.tcp;

import com.ixtens.dto.ResponseDto;

import java.net.InetAddress;

public interface Connection {

    InetAddress getAddress();

    void send(ResponseDto responseDto);

    void start();

    void close();
}
