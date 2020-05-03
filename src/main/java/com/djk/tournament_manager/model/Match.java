package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Match {
    private final UUID id;
    private UUID tournamentID;

    @NotBlank
    private Player player1;
    private Player player2;
    private int numGames;
    private int player1Wins;
    private int player2Wins;

    public Match(@JsonProperty("id") UUID id,
                  @JsonProperty("tmtID") UUID tournamentID,
                 @JsonProperty("numGames") int numGames,
                  @JsonProperty("player1") Player player1,
                  @JsonProperty("player2") Player player2)
    {
        this.id = id;
        this.tournamentID = tournamentID;
        this.numGames = numGames;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Wins = 0;
        this.player2Wins = 0;

    }


    public UUID getID()
    {
        return this.id;
    }

    public UUID getTournamentID() {return this.tournamentID; }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2()
    {
        return this.player2;
    }

    public int getPlayer1Wins() {
        return this.player1Wins;
    }

    public int getPlayer2Wins()
    {
        return this.player2Wins;
    }

    public int getNumGames()
    {
        return this.numGames;
    }

    public void setTournamentID(UUID tournamentID) {this.tournamentID = tournamentID; }

    public boolean addPlayer1Win()
    {
        this.player1Wins++;

        if(this.player1Wins + this.player2Wins == numGames)
        {
            return true;
        }

        return false;
    }

    public boolean addPlayer2Win()
    {
        this.player2Wins++;

        if(this.player1Wins + this.player2Wins == numGames)
        {
            return true;
        }

        return false;
    }

    public void didDraw()
    {
        // TODO
    }

    public boolean playerIsInMatch(UUID playerID)
    {
        return (playerID == this.player1.getID() || playerID == this.player2.getID());
    }
}
