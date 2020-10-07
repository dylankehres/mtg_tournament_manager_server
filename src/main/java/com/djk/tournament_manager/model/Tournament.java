package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class Tournament {

    @JsonProperty("id") private final String id;

    @NotBlank
    @JsonProperty("name") private final String name;
    @JsonProperty("roomCode") private final String roomCode;
    @JsonProperty("format") private final String format;
    @JsonProperty("numRounds") private final int numRounds;
    @JsonProperty("numGames") private final int numGames;
    @JsonProperty("currRound") private int currRound;

    public Tournament()
    {
        this.id = "";
        this.name = "";
        this.roomCode = "";
        this.format = "";
        this.numRounds = 0;
        this.numGames = 0;
        this.currRound = 0;
    }

    public Tournament(String id,
                      String name,
                      String roomCode,
                      String format,
                      int numRounds,
                      int numGames)
    {
        this.id = id;
        this.name = name;
        this.roomCode = roomCode;
        this.format = format;
        this.numRounds = numRounds;
        this.numGames = numGames;
        this.currRound = 0;
    }


    public String getID() { return this.id; }

    public String getName() { return this.name; }

    public String getRoomCode() { return this.roomCode;}

    public String getFormat() { return this.format; }

    public int getNumRounds() { return this.numRounds; }

    public int getNumGames() { return this.numGames; }

    public int getCurrRound() { return this.currRound; }
    public void incrementCurrRound() { this.currRound++; }
}
