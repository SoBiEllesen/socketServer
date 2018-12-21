package com.ixtens.server.tcp;

import com.ixtens.dto.RequestDto;
import com.ixtens.dto.ResponseDto;
import com.ixtens.dto.Result;
import com.ixtens.server.service.TaskForwarderService;
import com.ixtens.server.service.exception.ProblemsWithReflectionException;
import com.ixtens.server.service.exception.WorkerMethodNotFoundException;
import com.ixtens.server.service.exception.WorkerNotFoundException;
import com.ixtens.server.service.exception.WrongParametersCountException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TcpRequestProcessor implements Connection.Listener {

    private static Log logger = LogFactory.getLog(TcpRequestProcessor.class);

    private final TaskForwarderService taskForwarderService;

    @Autowired
    public TcpRequestProcessor(TaskForwarderService taskForwarderService) {
        this.taskForwarderService = taskForwarderService;
    }

    @Override
    @Async
    public void messageReceived(Connection connection, RequestDto requestDto) {
        logger.info("start processing request " + requestDto.getId());
        try {
            Result result = taskForwarderService.forwardWork(requestDto);
            ResponseDto responseDto = new ResponseDto(requestDto.getId(), result);
            connection.send(responseDto);
        } catch (WorkerNotFoundException | WorkerMethodNotFoundException | WrongParametersCountException | ProblemsWithReflectionException e) {
            logger.error(e);
            connection.send(new ResponseDto(requestDto.getId(), e.getMessage()));
        } catch (Exception e) {
            logger.error(e);
            connection.send(new ResponseDto(requestDto.getId(), e.getMessage()));
        }
        logger.info("end processing request " + requestDto.getId());
    }

    @Override
    public void connected(Connection connection) {

    }

    @Override
    public void disconnected(Connection connection) {

    }
}
