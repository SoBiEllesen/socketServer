package com.ixtens.dto;

import java.util.Objects;

public class RequestDto extends Dto {

    private String service;
    private String method;
    private Request request;

    public RequestDto(Integer id, String service, String method, Request request) {
        super(id);
        this.service = service;
        this.method = method;
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RequestDto that = (RequestDto) o;
        return Objects.equals(service, that.service) &&
               Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, method);
    }

    @Override
    public String toString() {
        return "RequestDto{" +
               "id=" + getId() +
               " service='" + service + '\'' +
               ", method='" + method + '\'' +
               ", request=" + request +
               '}';
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
