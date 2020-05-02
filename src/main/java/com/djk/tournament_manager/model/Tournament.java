package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.*;

public class Tournament {

    private final UUID id;

    @NotBlank
    private final String tmtName;
    private final String roomCode;
    private final String format;

    public Tournament(@JsonProperty("id") UUID id,
                      @JsonProperty("tmtName") String tmtName,
                      @JsonProperty("roomCode") String roomCode,
                      @JsonProperty("format") String format)
    {
        this.id = id;
        this.tmtName = tmtName;
        this.roomCode = roomCode;
        this.format = format;
    }


    public UUID getID()
    {
        return this.id;
    }

    public String getName()
    {
        return this.tmtName;
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
