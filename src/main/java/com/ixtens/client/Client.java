package com.ixtens.client;

import com.ixtens.dto.Request;
import com.ixtens.dto.RequestDto;
import com.ixtens.dto.ResponseDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class Client implements AutoCloseable {

    private static Log logger = LogFactory.getLog(Client.class);

    private final int socketTimeout = Integer.MAX_VALUE;
    private Socket socket;
    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;
    private boolean stop = false;
    private Map<UUID, PairTimestampFuture<Object>> mapWithFuture = new ConcurrentHashMap<>();

    public Client(String host, int port) throws IOException {
        logger.info("start create client");
        socket = new Socket(host, port);
        socket.setSoTimeout(socketTimeout);
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new ObjectInputStream(socket.getInputStream());
        unlimitedRead();
        cacheCleaner();
        logger.info("end client client");
    }

    public Object remoteCall(String serviceName, String methodName, Object[] params) {
        if (stop){
            return null;
        }
        RequestDto requestDto = new RequestDto(UUID.randomUUID(), serviceName, methodName, new Request(params));
        logger.info("start new remoteCall with " + requestDto.toString());
        CompletableFuture<Object> future = new CompletableFuture<>();
        mapWithFuture.put(requestDto.getId(), new PairTimestampFuture<>(future));
        try {
            sendDataToServer(requestDto);
        } catch (IOException e) {
            logger.error(e);
        }
        Object result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e);
        }
        if( result == null){
            logger.info("not response for id " + requestDto.getId());
        }else {
            logger.info("get response " + result.toString());
        }
        return result;
    }

    private synchronized void sendDataToServer(RequestDto requestDto) throws IOException {
        toServer.writeObject(requestDto);
    }

    private void cacheCleaner() {
        new Thread(() -> {
            logger.debug("start endless clean cache");
            while (!stop) {
                logger.debug("trying to clean futures map size is " + mapWithFuture.size());
                mapWithFuture.keySet().stream()
                        .filter(uuid -> mapWithFuture.get(uuid).isExpired())
                        .forEach(uuid -> {
                            mapWithFuture.get(uuid).getFuture().complete(new ResponseDto(uuid, "Socket Timeout"));
                            logger.debug("future with uuid " + uuid + " was removed from map");
                            mapWithFuture.remove(uuid);
                        });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }
        }).start();
    }

    private void unlimitedRead() {
        new Thread(() -> {
            logger.debug("start unlimited read");
            while (!stop) {
                try {
                    logger.debug("in unlimited read map size " + mapWithFuture.size());
                    if (!mapWithFuture.isEmpty()) {
                        ResponseDto response = (ResponseDto) fromServer.readObject();
                        if (response.getId() != null && mapWithFuture.containsKey(response.getId())) {
                            CompletableFuture<Object> future = mapWithFuture.get(response.getId()).getFuture();
                            future.complete(response);
                            mapWithFuture.remove(response.getId());
                        }
                    } else {
                        Thread.sleep(1000);
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    logger.error(e);
                    stop = true;
                }
            }
        }).start();
    }

    @Override
    public void close() throws InterruptedException, IOException {
        logger.info("In close Client");
        while (!mapWithFuture.isEmpty()) {
            Thread.sleep(500);
        }
        logger.info("start close client");
        stop = true;
        toServer.close();
        fromServer.close();
        socket.close();
    }
}
