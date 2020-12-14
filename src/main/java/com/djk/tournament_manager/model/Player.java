package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Player extends BaseModel {

    @JsonProperty("tournamentID") private String tournamentID;
    @JsonProperty("name")  private String name;
    @JsonProperty("roomCode") private String roomCode;
    @JsonProperty("deckName") private String deckName;
    @JsonProperty("deckList") private String deckList;
    @JsonProperty("points") private int points;
    @JsonProperty("bye") private boolean bye;

    public Player()
    {
        this.tournamentID = "";
        this.name = "";
        this.roomCode = "";
        this.deckName = "";
        this.deckList = "";
        this.points = 0;
        this.bye = false;
    }

    public Player(String tournamentID,
                  String name,
                  String roomCode,
                  String deckName,
                  String deckList,
                  boolean bye)
    {
        super();

        this.tournamentID = tournamentID;
        this.name = name;
        this.roomCode = roomCode;
        this.deckName = deckName;
        this.deckList = deckList;
        this.points = 0;
        this.bye = bye;
    }

    public String getTournamentID() {return this.tournamentID; }

    public String getName()
    {
        return this.name;
    }

    public String getRoomCode() { return this.roomCode; }

    public String getDeckList()
    {
        return this.deckList;
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
