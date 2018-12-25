package com.ixtens.server.service.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Worker4Impl {

    private static Log logger = LogFactory.getLog(Worker4Impl.class);

    public Integer manyParams(Integer param1, Integer param2) {
        return param1 + param2;
    }
}
