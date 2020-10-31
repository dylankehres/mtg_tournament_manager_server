package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseModel {
    @JsonProperty("id") private String id;
    @JsonProperty("active") private boolean active;

    public BaseModel()
    {
        this.id = "";
        this.active = true;
    }

    public BaseModel(String id)
    {
        this.id = id;
        this.active = true;
    }

    public String getID() { return this.id; };

    public void setID(String id) {
        this.id = id;
    }

    public boolean getActive() { return this.active; }
}
