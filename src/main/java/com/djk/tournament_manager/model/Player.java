package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Player {

    private final String id;
    private String tournamentID;

    @NotBlank
    private String name;
    private String roomCode;
    private String format;
    private String deckName;

    public Player()
    {
        this.id = "";
        this.tournamentID = "";
        this.name = "";
        this.roomCode = "";
        this.format = "";
        this.deckName = "";
    }

    public Player(@JsonProperty("id") String id,
                  @JsonProperty("tmtID") String tournamentID,
                  @JsonProperty("name") String name,
                  @JsonProperty("roomCode") String roomCode,
                  @JsonProperty("format") String format,
                  @JsonProperty("deckName") String deckName)
    {
        this.id = id;
        this.tournamentID = tournamentID;
        this.name = name;
        this.roomCode = roomCode;
        this.format = format;
        this.deckName = deckName;
    }

//    public Player(@JsonProperty("id") String id,
//                  @JsonProperty("tmtID") String tournamentID,
//                  @JsonProperty("roomCode") String roomCode,
//                  @JsonProperty("format") String format)
//    {
//        this.id = id;
//        this.tournamentID = tournamentID;
//        this.userName = "BYE";
//        this.roomCode = roomCode;
//        this.format = format;
//        this.deckName = "";
//    }

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

    public void setTournamentID(String tournamentID) {this.tournamentID = tournamentID; }
}
