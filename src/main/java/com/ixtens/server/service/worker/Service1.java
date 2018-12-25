package com.ixtens.server.service.worker;

import java.util.Date;

public class Service1 {

    public void sleep() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    public Date getCurrentDate() {
        return new Date();
    }
}
