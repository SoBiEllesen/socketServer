package com.ixtens.server.service;

import com.ixtens.dto.RequestDto;
import com.ixtens.server.service.exception.ProblemsWithReflectionException;
import com.ixtens.server.service.exception.WorkerMethodNotFoundException;
import com.ixtens.server.service.exception.WorkerNotFoundException;
import com.ixtens.server.service.exception.WrongParametersCountException;
import com.ixtens.server.service.worker.WorkerLoader;
import com.ixtens.server.tcp.TcpServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskForwarderService {

    private static Log logger = LogFactory.getLog(TcpServer.class);

    private Map<String, Object> mapWithWorkers = new HashMap<>();

    @PostConstruct
    public void postConstruct() throws IOException {
        mapWithWorkers = WorkerLoader.loadWorkers();
        logger.info("Loaded " + mapWithWorkers.size() + " workers");
    }

    public Object forwardWork(RequestDto requestDto)
            throws WorkerNotFoundException, WorkerMethodNotFoundException, WrongParametersCountException, ProblemsWithReflectionException {
        Object workerService = Optional.ofNullable(mapWithWorkers.get(requestDto.getService()))
                .orElseThrow(() -> new WorkerNotFoundException(requestDto.getService()));
        Method method = Arrays
                .stream(workerService.getClass().getDeclaredMethods())
                .filter(methodCandidate -> methodCandidate.getName().equals(requestDto.getMethod()))
                .findAny()
                .orElseThrow(() -> new WorkerMethodNotFoundException(requestDto.getService(), requestDto.getMethod()));
        method.setAccessible(true);
        int parameterCount = method.getParameterCount();
        if (parameterCount != requestDto.getRequest().getParams().length) {
            throw new WrongParametersCountException(parameterCount, requestDto.getRequest().getParams().length);
        }
        try {
            if (parameterCount == 0) {
                return method.invoke(workerService);
            } else {
                return method.invoke(workerService, requestDto.getRequest().getParams());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
            throw new ProblemsWithReflectionException();
        }
    }
}
