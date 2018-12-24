package com.ixtens.client;

public class AllServiceCaller implements Runnable {

    private final Client client;

    public AllServiceCaller(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        client.remoteCall("service1", "doWork", new Object[]{});
        client.remoteCall("serviceSleep", "doSlowWork", new Object[]{});
        client.remoteCall("serviceWrongParameter", "doWrongParameter", new Object[]{1,2,3,4});
        client.remoteCall("serviceManyParameters", "manyParams", new Object[]{1,2});
        client.remoteCall("testService", "getCurrentDate", new Object[]{});
        client.remoteCall("service3", "wrongService", new Object[]{});
    }
}
