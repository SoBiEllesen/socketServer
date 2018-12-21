package com.ixtens.server.service.worker;

import com.ixtens.dto.Result;
import com.ixtens.server.service.WorkerService;

import java.util.Date;

public class Service1 implements WorkerService {

    public void sleep() {
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Result getCurrentDate() {
        return new Result(new Date());
    }
}
