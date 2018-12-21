package com.ixtens.dto;

import java.io.Serializable;
import java.util.UUID;

public abstract class Dto implements Serializable {

    private UUID id;

    public Dto(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
