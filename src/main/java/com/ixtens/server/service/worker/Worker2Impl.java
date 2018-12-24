package com.ixtens.server.service.worker;

import com.ixtens.dto.Request;
import com.ixtens.dto.Result;
import com.ixtens.server.service.WorkerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Worker2Impl implements WorkerService {

    private static Log logger = LogFactory.getLog(Worker1Impl.class);

    public Worker2Impl() {
    }

    public Result doSlowWork() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
