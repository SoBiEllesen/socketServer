package com.ixtens.server.service.worker;

import com.ixtens.dto.Result;
import com.ixtens.server.service.WorkerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Worker4Impl implements WorkerService {

    private static Log logger = LogFactory.getLog(Worker4Impl.class);

    public Result manyParams(Integer param1, Integer param2) {
        return new Result(param1 + param2);
    }
}
