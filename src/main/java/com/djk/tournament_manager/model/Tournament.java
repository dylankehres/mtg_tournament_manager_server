package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.*;

public class Tournament {

    private final String id;

    @NotBlank
    private final String name;
    private final String roomCode;
    private final String format;

    public Tournament()
    {
        this.id = "";
        this.name = "";
        this.roomCode = "";
        this.format = "";
    }

    public Tournament(@JsonProperty("id") String id,
                      @JsonProperty("name") String name,
                      @JsonProperty("roomCode") String roomCode,
                      @JsonProperty("format") String format)
    {
        this.id = id;
        this.name = name;
        this.roomCode = roomCode;
        this.format = format;
    }


    public String getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getRoomCode() { return this.roomCode;}

    public String getFormat()
    {
        return this.format;
    }

//    public List<Player> getPlayerList() {return this.playerList; }
//
//    public void addPlayer(Player player) {
//        this.playerList.add(player);
//    }
}
