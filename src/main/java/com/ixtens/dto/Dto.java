package com.ixtens.dto;

import java.io.Serializable;

public abstract class Dto implements Serializable {

    private Integer id;

    public Dto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
