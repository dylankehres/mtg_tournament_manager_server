package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class Player extends BaseModel {

    @JsonProperty("tournamentID") private String tournamentID;
    @JsonProperty("name")  private String name;
    @JsonProperty("roomCode") private String roomCode;
    @JsonProperty("format") private String format;
    @JsonProperty("deckName") private String deckName;
    @JsonProperty("points") private int points;
    @JsonProperty("bye") private boolean bye;

    public Player()
    {
        this.tournamentID = "";
        this.name = "";
        this.roomCode = "";
        this.format = "";
        this.deckName = "";
        this.points = 0;
        this.bye = false;
    }

    public Player(String tournamentID,
                  String name,
                  String roomCode,
                  String format,
                  String deckName,
                  boolean bye)
    {
        super();

        this.tournamentID = tournamentID;
        this.name = name;
        this.roomCode = roomCode;
        this.format = format;
        this.deckName = deckName;
        this.points = 0;
        this.bye = bye;
    }

    public String getTournamentID() {return this.tournamentID; }

    public String getName()
    {
        return this.name;
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

    public int getPoints() { return this.points; }

    public boolean getBye() { return this.bye; }
    public void setBye(boolean bye) { this.bye = bye; }

    public void setTournamentID(String tournamentID) {this.tournamentID = tournamentID; }

    public void addPoints(int addPoints) {
        this.points+= addPoints;
    }

    public void addWinPoints(boolean shutout) {
        if(shutout) {
            this.points += 3;
        } else {
            this.points += 2;
        }
    }

    public void addDrawPoints() {
        this.points ++;
    }
}
