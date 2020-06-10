package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class Match {
    private final String id;
    private String tournamentID;

    @NotBlank
    private String player1ID;
    private String player2ID;
    private int numGames;
    private int player1Wins;
    private int player2Wins;
    private int tableNum;

    public Match()
    {
        this.id = null;
        this.tournamentID = null;
        this.numGames = 0;
        this.player1ID = "";
        this.player2ID = "";
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.tableNum = 0;
    }

    public Match(@JsonProperty("id") String id,
                 @JsonProperty("tmtID") String tournamentID,
                 @JsonProperty("numGames") int numGames,
                 @JsonProperty("player1ID") String player1ID,
                 @JsonProperty("player2ID") String player2ID,
                 @JsonProperty("tableNum") int tableNum)
    {
        this.id = id;
        this.tournamentID = tournamentID;
        this.numGames = numGames;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.tableNum = tableNum;
    }


    public String getID()
    {
        return this.id;
    }

    public String getTournamentID() {return this.tournamentID; }

    public String getPlayer1ID() { return this.player1ID; }

    public String getPlayer2ID()
    {
        return this.player2ID;
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

    public int getTableNum() { return this.tableNum; }

    public void setTournamentID(String tournamentID) {this.tournamentID = tournamentID; }

    public void setTableNum(int tableNum) { this.tableNum = tableNum; }

    public boolean addPlayerWin(String playerID) {
        if(playerID.equals(this.getPlayer1ID())) {
            this.player1Wins++;
        }
        else if (playerID.equals(this.getPlayer2ID())){
            this.player2Wins++;
        }
        else {
            System.out.println("ERROR: Unable to increment player win  (player ID: " + playerID + ")");
        }

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

    public boolean playerIsInMatch(String playerID)
    {
        return (this.player1ID.equals(playerID) || this.player2ID.equals(playerID));
    }
}
