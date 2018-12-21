package com.ixtens.dto;

import java.io.Serializable;

public class Request implements Serializable {

    private Object [] params;

    public Request(Object[] params) {
        this.params = params;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

}
