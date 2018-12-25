package com.ixtens.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutionException;

public class Caller2 implements Runnable {

    private static Log logger = LogFactory.getLog(Caller2.class);

    private Client client;

    public Caller2(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                logger.info(client.remoteCall("testService", "getCurrentDate", new Object[]{}));
                logger.info(client.remoteCall("testService", "sleep", new Object[]{}));
            } catch (ExecutionException | InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
