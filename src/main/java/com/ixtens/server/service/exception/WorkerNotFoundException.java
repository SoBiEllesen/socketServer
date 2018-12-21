package com.ixtens.server.service.exception;

public class WorkerNotFoundException extends java.lang.ClassNotFoundException {

    public WorkerNotFoundException(String service) {
        super("Service with name " + service + " not found");
    }
}
