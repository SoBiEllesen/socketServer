package com.ixtens.server.service.exception;

public class WorkerMethodNotFoundException extends NoSuchMethodException{

    public WorkerMethodNotFoundException(String serviceName, String methodName){
        super("method " + methodName + " not found in service " + serviceName);
    }

}
