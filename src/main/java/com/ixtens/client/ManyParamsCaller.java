package com.ixtens.client;

import java.util.concurrent.ExecutionException;

public class ManyParamsCaller implements Runnable {

    private final Client client;

    public ManyParamsCaller(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            client.remoteCall("serviceManyParameters", "manyParams", new Object[]{1, 2});
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
