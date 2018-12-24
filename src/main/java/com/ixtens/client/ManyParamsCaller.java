package com.ixtens.client;

public class ManyParamsCaller implements Runnable{

    private final Client client;

    public ManyParamsCaller(Client client){
        this.client = client;
    }

    @Override
    public void run() {
        client.remoteCall("serviceManyParameters", "manyParams", new Object[]{1, 2});
    }
}
