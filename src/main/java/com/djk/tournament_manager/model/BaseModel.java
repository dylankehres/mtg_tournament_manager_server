package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseModel {
    @JsonProperty("id") private final String id;

    public BaseModel()
    {
        this.id = "";
    }

    public String getID() { return this.id; };
}
