package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Player {

    private final UUID id;
    private UUID tournamentID;

    @NotBlank
    private String userName;
    private String roomCode;
    private String format;
    private String deckName;

    public Player(@JsonProperty("id") UUID id,
                  @JsonProperty("tmtID") UUID tournamentID,
                  @JsonProperty("userName") String userName,
                  @JsonProperty("roomCode") String roomCode,
                  @JsonProperty("format") String format,
                  @JsonProperty("deckName") String deckName)
    {
        this.id = id;
        this.tournamentID = tournamentID;
        this.userName = userName;
        this.roomCode = roomCode;
        this.format = format;
        this.deckName = deckName;
    }

//    public Player(@JsonProperty("id") UUID id,
//                  @JsonProperty("tmtID") UUID tournamentID,
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

    public UUID getID()
    {
        return this.id;
    }

    public UUID getTournamentID() {return this.tournamentID; }

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

    public void setTournamentID(UUID tournamentID) {this.tournamentID = tournamentID; }
}
