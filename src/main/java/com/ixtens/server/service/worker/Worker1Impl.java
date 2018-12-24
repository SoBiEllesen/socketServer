package com.ixtens.server.service.worker;

import com.ixtens.dto.Request;
import com.ixtens.dto.Result;
import com.ixtens.server.service.WorkerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Worker1Impl implements WorkerService {

    private static Log logger = LogFactory.getLog(Worker1Impl.class);

    public Worker1Impl() {

    }

    public Result doWork() {
        logger.info("in class worker1 method do work");
        Result result = new Result(1, "string from worker 1");
        return result;
    }
}
