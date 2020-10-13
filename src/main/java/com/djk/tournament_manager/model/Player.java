package com.djk.tournament_manager.model;

import com.djk.tournament_manager.dao.PlayerDao;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Player {

    @JsonProperty("id") private final String id;
    @JsonProperty("tournamentID") private String tournamentID;

    @NotBlank
    @JsonProperty("name")  private String name;
    @JsonProperty("roomCode") private String roomCode;
    @JsonProperty("format") private String format;
    @JsonProperty("deckName") private String deckName;
    @JsonProperty("points") private int points;

    public Player()
    {
        this.id = "";
        this.tournamentID = "";
        this.name = "";
        this.roomCode = "";
        this.format = "";
        this.deckName = "";
        this.points = 0;
    }

    public Player(String id)
    {
        this.id = id;
        this.tournamentID = "";
        this.name = "";
        this.roomCode = "";
        this.format = "";
        this.deckName = "";
        this.points = 0;
    }

    public Player(String id,
                  String tournamentID,
                  String name,
                  String roomCode,
                  String format,
                  String deckName)
    {
        this.id = id;
        this.tournamentID = tournamentID;
        this.name = name;
        this.roomCode = roomCode;
        this.format = format;
        this.deckName = deckName;
        this.points = 0;
    }

    public String getID()
    {
        return this.id;
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
