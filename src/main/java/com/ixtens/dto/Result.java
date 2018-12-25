package com.ixtens.dto;

import java.io.Serializable;

public class Result implements Serializable {

    private Integer resultInt;
    private String string;
    private Object data;

    public Result(Object data) {
        this.data = data;
    }

    public Integer getResultInt() {
        return resultInt;
    }

    public void setResultInt(Integer resultInt) {
        this.resultInt = resultInt;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return "Result{" +
               "resultInt=" + resultInt +
               ", string='" + string + '\'' +
               ", data=" + data +
               '}';
    }
}
