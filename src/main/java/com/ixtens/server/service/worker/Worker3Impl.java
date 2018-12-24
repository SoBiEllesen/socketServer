package com.ixtens.server.service.worker;

import com.ixtens.server.service.WorkerService;
import com.ixtens.server.service.exception.WrongParametersCountException;

public class Worker3Impl implements WorkerService {

    public void doWrongParameter(Object... args) {
        if (args.length != 2) {
            throw new WrongParametersCountException(2, args.length);
        }
    }
}
