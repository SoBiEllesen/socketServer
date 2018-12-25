package com.ixtens.dto;

public class ResponseDto extends Dto {

    private Result result;
    private String errorCode;

    public ResponseDto(Integer id, Result result) {
        super(id);
        this.result = result;
    }

    public ResponseDto(Integer id, String errorCode) {
        super(id);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
               "id=" + getId() +
               ", result=" + result +
               ", errorCode=" + errorCode +
               '}';
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
