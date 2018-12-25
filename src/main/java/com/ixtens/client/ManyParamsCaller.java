package com.ixtens.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutionException;

public class ManyParamsCaller implements Runnable {

    private static Log logger = LogFactory.getLog(ManyParamsCaller.class);

    private final Client client;

    public ManyParamsCaller(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            client.remoteCall("serviceManyParameters", "manyParams", new Object[]{1, 2});
        } catch (ExecutionException | InterruptedException e) {
            logger.error(e);
            Thread.currentThread().interrupt();
        }
    }
}
