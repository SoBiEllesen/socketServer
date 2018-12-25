package com.ixtens.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutionException;

public class Caller implements Runnable {

    private static Log logger = LogFactory.getLog(Caller.class);

    private Client client;

    public Caller(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                logger.info(client.remoteCall("testService", "sleep", new Object[]{}));
                logger.info(client.remoteCall("testService", "getCurrentDate", new Object[]{}));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
