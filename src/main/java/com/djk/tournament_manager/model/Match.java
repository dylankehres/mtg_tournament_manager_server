package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Match {
    @NotBlank
    private final String id;
    private final String tournamentID;
    private final String player1ID;
    private final String player2ID;
    private int player1Wins;
    private int player2Wins;
    private int tableNum;
    private List<String> gameKeys;
    private  String activeGameID;

    public Match()
    {
        this.id = null;
        this.tournamentID = null;
        this.player1ID = "";
        this.player2ID = "";
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.tableNum = 0;
        this.gameKeys = new ArrayList<>();
        this.activeGameID = "";
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
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.tableNum = tableNum;
        this.activeGameID = "";
        this.gameKeys = new ArrayList<>(numGames);
    }


    public String getID() { return this.id; }

    public String getTournamentID() {return this.tournamentID; }

    public String getPlayer1ID() { return this.player1ID; }

    public String getPlayer2ID() { return this.player2ID; }

    public int getPlayer1Wins() { return this.player1Wins; }

    public int getPlayer2Wins() { return this.player2Wins; }

    public List<String> getGameKeys() { return this.gameKeys; }

    public String activeGameID() { return this.activeGameID; }

    public int getTableNum() { return this.tableNum; }

    public void setTableNum(int tableNum) { this.tableNum = tableNum; }

    public void setGameKeys(List<String> gameKeys){ this.gameKeys = gameKeys; }

    public void setActiveGameKey(String gameKey){ this.activeGameID = gameKey; }

    public void addNewActiveGameKey(String gameID) {
//        String [] newGameKeys = new String[this.gameKeys.length + 1];
//
//        for(int x = 0; x<this.gameKeys.length; x++) {
//            newGameKeys[x] = this.gameKeys[x];
//        }
//
//        newGameKeys[this.gameKeys.length + 1] = gameKey;
        this.gameKeys.add(gameID);
        this.activeGameID = gameID;
//        this.gameKeys = newGameKeys;
    }

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

        return this.player1Wins + this.player2Wins == this.gameKeys.size();
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
