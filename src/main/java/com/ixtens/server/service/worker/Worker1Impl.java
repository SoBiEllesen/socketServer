package com.ixtens.server.service.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Worker1Impl {

    private static Log logger = LogFactory.getLog(Worker1Impl.class);

    public Worker1Impl() {

    }

    public String doWork() {
        logger.info("in class worker1 method do work");
        return "string from worker 1";
    }
}
