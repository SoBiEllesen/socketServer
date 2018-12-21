package com.ixtens.server.service.exception;

import java.security.InvalidParameterException;

public class WrongParametersCountException extends InvalidParameterException {

    public WrongParametersCountException(int expected, int found) {
        super("Wrong parameter count find. Expected " + expected + " , but found " + found);
    }
}
