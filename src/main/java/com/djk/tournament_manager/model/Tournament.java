package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.*;

public class Tournament {

    private final UUID id;

    @NotBlank
    private final String name;

    public Tournament(@JsonProperty("id") UUID id,
                      @JsonProperty("name") String name)
    {
        this.id = id;
        this.name = name;
    }


    public UUID getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

}
