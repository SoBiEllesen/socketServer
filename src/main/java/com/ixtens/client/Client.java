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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class Client implements AutoCloseable {

    private static Log logger = LogFactory.getLog(Client.class);

    private final int socketTimeout = Integer.MAX_VALUE;
    private Socket socket;
    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;
    private boolean stop = false;
    private AtomicInteger idGenerator = new AtomicInteger(0);
    private Map<Integer, PairTimestampFuture<Object>> mapWithFuture = new ConcurrentHashMap<>();

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

    public Object remoteCall(String serviceName, String methodName, Object[] params) throws ExecutionException, InterruptedException {
        if (stop) {
            return null;
        }
        RequestDto requestDto = new RequestDto(idGenerator.getAndIncrement(), serviceName, methodName, new Request(params));
        logger.info("start new remoteCall with " + requestDto.toString());
        CompletableFuture<Object> future = new CompletableFuture<>();
        mapWithFuture.put(requestDto.getId(), new PairTimestampFuture<>(future));
        try {
            sendDataToServer(requestDto);
        } catch (IOException e) {
            logger.error(e);
        }
        Object result = future.get();
        if (result == null) {
            logger.info("not response for id " + requestDto.getId());
        } else {
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
                if (!Thread.interrupted()) {
                    logger.debug("trying to clean futures map size is " + mapWithFuture.size());
                    mapWithFuture.keySet().stream()
                            .filter(uuid -> mapWithFuture.get(uuid).isExpired())
                            .forEach(id -> {
                                mapWithFuture.get(id).getFuture().complete(new ResponseDto(id, "Future removed from waiting map"));
                                logger.debug("future with id " + id + " was removed from map");
                                mapWithFuture.remove(id);
                            });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error(e);
                        Thread.currentThread().interrupt();
                        break;
                    }
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
                    ResponseDto response = (ResponseDto) fromServer.readObject();
                    if (response.getId() != null && mapWithFuture.containsKey(response.getId())) {
                        CompletableFuture<Object> future = mapWithFuture.get(response.getId()).getFuture();
                        future.complete(response);
                        mapWithFuture.remove(response.getId());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.error(e);
                    stop = true;
                }
            }
        }).start();
    }

    @Override
    public void close() throws IOException {
        logger.info("start close client");
        stop = true;
        toServer.close();
        fromServer.close();
        socket.close();
        logger.info("client is closed");
    }
}
