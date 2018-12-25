package com.ixtens.server.service.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Worker2Impl {

    private static Log logger = LogFactory.getLog(Worker1Impl.class);

    public Worker2Impl() {
    }

    public void doSlowWork() throws InterruptedException {
        Thread.sleep(10000);
    }
}
