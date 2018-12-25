package com.ixtens.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientSocket {

    private static Log logger = LogFactory.getLog(ClientSocket.class);

    public static void main(String[] args) {
        try (Client client = new Client("localhost", 9090)) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            //executorService.execute(new Caller2(client));
            //executorService.execute(new ManyParamsCaller(client));
            //executorService.execute(new AllServiceCaller(client));
            for (int i = 0; i < 1000; i++) {
                executorService.execute(new Caller2(client));
            }
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            logger.error(e);
        }
    }
}