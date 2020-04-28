package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Player {

    private final UUID id;

    @NotBlank
    private final String userName;
    private final String roomCode;
    private final String format;
    private final String deckName;

    public Player(@JsonProperty("id") UUID id,
                  @JsonProperty("userName") String userName,
                  @JsonProperty("roomCode") String roomCode,
                  @JsonProperty("format") String format,
                  @JsonProperty("deckName") String deckName)
    {
        this.id = id;
        this.userName = userName;
        this.roomCode = roomCode;
        this.format = format;
        this.deckName = deckName;
    }


    public UUID getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.userName;
    }

    public String getRoomCode() { return this.roomCode; }

    public String getFormat()
    {
        return this.format;
    }

    public String getDeckName()
    {
        return this.deckName;
    }
}
