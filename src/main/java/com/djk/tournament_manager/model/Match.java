package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Match {
    @JsonProperty("id")  private final String id;
    @JsonProperty("tmtID") private final String tournamentID;
    @JsonProperty("player1ID") private final String player1ID;
    @JsonProperty("player2ID") private final String player2ID;
    @JsonProperty("player1Wins") private int player1Wins;
    @JsonProperty("player2Wins") private int player2Wins;
    @JsonProperty("player1Ready") private boolean player1Ready;
    @JsonProperty("player2Ready") private boolean player2Ready;
    @JsonProperty("tableNum") private int tableNum;
    @JsonProperty("active") private boolean active;
    @JsonProperty("gameKeys") private List<String> gameKeys;
    @JsonProperty("activeGameID") private String activeGameID;
    @JsonProperty("startTime") private long startTime;
    @JsonProperty("endTime") private long endTime;
    @JsonProperty("timeLimit") private long timeLimit;

    public Match()
    {
        this.id = null;
        this.tournamentID = null;
        this.player1ID = "";
        this.player2ID = "";
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.player1Ready = false;
        this.player2Ready = false;
        this.tableNum = 0;
        this.active = false;
        this.gameKeys = new ArrayList<>();
        this.activeGameID = "";
        this.startTime = 0;
        this.endTime = 0;
        this.timeLimit = 0;
    }

    public Match(String id, String tournamentID, int numGames, String player1ID, String player2ID, int tableNum)
    {
        this.id = id;
        this.tournamentID = tournamentID;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.player1Ready = false;
        this.player2Ready = false;
        this.tableNum = tableNum;
        this.active = false;
        this.activeGameID = "";
        this.gameKeys = new ArrayList<>(numGames);
        this.startTime = 0;
        this.endTime = 0;
        this.timeLimit = 3000000;
    }


    public String getID() { return this.id; }

    public String getTournamentID() {return this.tournamentID; }

    public String getPlayer1ID() { return this.player1ID; }

    public String getPlayer2ID() { return this.player2ID; }

    public int getPlayer1Wins() { return this.player1Wins; }

    public int getPlayer2Wins() { return this.player2Wins; }

    public boolean getPlayer1Ready() { return this.player1Ready; }

    public boolean getPlayer2Ready() { return this.player2Ready; }

    public List<String> getGameKeys() { return this.gameKeys; }

    public String getActiveGameID() { return this.activeGameID; }

    public int getTableNum() { return this.tableNum; }

    public boolean getActive() { return this.active; }

    public long getStartTime() { return this.startTime; }

    public long getEndTime() { return this.endTime; }

    public long getTimeLimit() { return this.timeLimit; }

    public void setTableNum(int tableNum) { this.tableNum = tableNum; }

    public void setActive(boolean active) { this.active = active; }

    public void setGameKeys(List<String> gameKeys){ this.gameKeys = gameKeys; }

    public void setActiveGameKey(String gameKey){ this.activeGameID = gameKey; }

    public void addNewActiveGameKey(String gameID) {
        this.gameKeys.add(gameID);
        this.activeGameID = gameID;
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

    public void playerReady(String playerID) {
        if (playerID.equals(getPlayer1ID())) {
            player1Ready = true;
        } else if (playerID.equals(getPlayer2ID())) {
            player2Ready = true;
        } else {
            System.err.println("Invalid player ready for game " + playerID);
        }

        if(player1Ready && player2Ready) {
            active = true;
            startTime = System.currentTimeMillis();
            endTime = startTime + timeLimit;
        }
    }

    public boolean playerIsInMatch(String playerID)
    {
        return (this.player1ID.equals(playerID) || this.player2ID.equals(playerID));
    }
}
