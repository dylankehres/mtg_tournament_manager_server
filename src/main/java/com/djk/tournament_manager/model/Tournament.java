package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class Tournament {

    @JsonProperty("id") private final String id;

    @NotBlank
    @JsonProperty("name") private final String name;
    @JsonProperty("roomCode") private final String roomCode;
    @JsonProperty("format") private final String format;
    @JsonProperty("rounds") private final int rounds;
    @JsonProperty("games") private final int games;

    public Tournament()
    {
        this.id = "";
        this.name = "";
        this.roomCode = "";
        this.format = "";
        this.rounds = 0;
        this.games = 0;
    }

    public Tournament(String id,
                      String name,
                      String roomCode,
                      String format,
                      int rounds,
                      int games)
    {
        this.id = id;
        this.name = name;
        this.roomCode = roomCode;
        this.format = format;
        this.rounds = rounds;
        this.games = games;
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

    public int getRounds() { return this.rounds; }

    public int getGames() {return this.games; }
}
