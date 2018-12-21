package com.ixtens.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Caller2 implements Runnable{

    private static Log logger = LogFactory.getLog(Caller2.class);

    private Client client;

    public Caller2(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        for (int i = 0; i< 10; i++){
            logger.info(client.remoteCall("testService", "getCurrentDate", new Object[]{}));
            logger.info(client.remoteCall("testService", "sleep", new Object[]{}));
        }
    }
}
