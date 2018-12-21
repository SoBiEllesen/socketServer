package com.ixtens.server.service.worker;

import com.ixtens.dto.Request;
import com.ixtens.server.service.WorkerService;
import com.ixtens.server.service.exception.WrongParametersCountException;

public class Worker3Impl implements WorkerService {

    public void doWrongParameter(Request request){
        if (request.getParams().length != 2){
            throw new WrongParametersCountException(2, request.getParams().length);
        }
    }

}
