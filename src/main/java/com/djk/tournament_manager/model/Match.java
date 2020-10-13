package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Match {
    @JsonProperty("id")  private final String id;
    @JsonProperty("tournamentID") private final String tournamentID;
    @JsonProperty("player1ID") private final String player1ID;
    @JsonProperty("player2ID") private final String player2ID;
    @JsonProperty("player1Wins") private int player1Wins;
    @JsonProperty("player2Wins") private int player2Wins;
    @JsonProperty("draws") private int draws;
    @JsonProperty("player1Ready") private boolean player1Ready;
    @JsonProperty("player2Ready") private boolean player2Ready;
    @JsonProperty("tableNum") private int tableNum;
    @JsonProperty("active") private boolean active;
    @JsonProperty("matchStatus") private int matchStatus;
    @JsonProperty("gameKeys") private List<String> gameKeys;
    @JsonProperty("activeGameID") private String activeGameID;
    @JsonProperty("startTime") private long startTime;
    @JsonProperty("endTime") private long endTime;
    @JsonProperty("timeLimit") private long timeLimit;
    @JsonProperty("roundNum") private final int roundNum;
    @JsonProperty("numGames") private final int numGames;

    public Match()
    {
        this.id = null;
        this.tournamentID = null;
        this.player1ID = "";
        this.player2ID = "";
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.draws = 0;
        this.player1Ready = false;
        this.player2Ready = false;
        this.tableNum = 0;
        this.active = true;
        this.matchStatus = MatchStatus.AwaitingPlayers.ordinal();
        this.gameKeys = new ArrayList<>();
        this.activeGameID = "";
        this.startTime = 0;
        this.endTime = 0;
        this.timeLimit = 0;
        this.roundNum = 0;
        this.numGames = 0;
    }

    public Match(String id, String tournamentID, int numGames, String player1ID, String player2ID, int tableNum, int roundNum)
    {
        this.id = id;
        this.tournamentID = tournamentID;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
        this.draws = 0;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.player1Ready = false;
        this.player2Ready = false;
        this.tableNum = tableNum;
        this.active = true;
        this.matchStatus = MatchStatus.AwaitingPlayers.ordinal();
        this.activeGameID = "";
        this.gameKeys = new ArrayList<>();
        this.startTime = 0;
        this.endTime = 0;
        this.timeLimit = 3000000;
        this.roundNum = roundNum;
        this.numGames = numGames;
    }

    public enum MatchStatus {
        AwaitingPlayers, InProgress, Complete
    };

    public String getID() { return this.id; }

    public String getTournamentID() {return this.tournamentID; }

    public String getPlayer1ID() { return this.player1ID; }

    public String getPlayer2ID() { return this.player2ID; }

    public int getPlayer1Wins() { return this.player1Wins; }

    public int getPlayer2Wins() { return this.player2Wins; }

    public int getDraws() { return this.draws; }

    public boolean getPlayer1Ready() { return this.player1Ready; }

    public boolean getPlayer2Ready() { return this.player2Ready; }

    public List<String> getGameKeys() { return this.gameKeys; }

    public String getActiveGameID() { return this.activeGameID; }

    public int getTableNum() { return this.tableNum; }
    
    public boolean getActive() { return this.active; }

    public int getMatchStatus() { return this.matchStatus; }

    public long getStartTime() { return this.startTime; }

    public long getEndTime() { return this.endTime; }

    public long getTimeLimit() { return this.timeLimit; }

    public int getRoundNum() { return this.roundNum; }

    public int getNumGames() { return this.numGames; }

    public void setTableNum(int tableNum) { this.tableNum = tableNum; }

//    public void setActive(boolean active) { this.active = active; }

    public void setMatchStatus(int status) { this.matchStatus = status; }

    public void setGameKeys(List<String> gameKeys){ this.gameKeys = gameKeys; }

    public void setActiveGameKey(String gameKey){ this.activeGameID = gameKey; }

    public void addNewActiveGameKey(String gameID) {
        this.gameKeys.add(gameID);
        this.activeGameID = gameID;
    }

    public boolean addPlayerWin(String playerID) {
        if(playerID.equals(this.getPlayer1ID())) {
            this.player1Wins++;

            // Has player1 won the match?
            double winRatio = (double) this.player1Wins / (double) this.numGames;
            if(winRatio > 0.5) {
                this.matchStatus = MatchStatus.Complete.ordinal();
            }
        }
        else if (playerID.equals(this.getPlayer2ID())){
            this.player2Wins++;

            // Has player2 won the match?
            double winRatio = (double) this.player2Wins / (double) this.numGames;
            if(winRatio > 0.5) {
                this.matchStatus = MatchStatus.Complete.ordinal();
            }
        }
        else {
            System.out.println("ERROR: Unable to increment player win  (player ID: " + playerID + ")");
        }

        return this.player1Wins + this.player2Wins == this.numGames;
    }

    public void didDraw()
    {
        this.draws++;

        // Were there more draws than wins?
        double winRatio = (double) this.player2Wins / (double) this.numGames;
        if(winRatio > 0.5) {
            this.matchStatus = MatchStatus.Complete.ordinal();
        }
        // Was the last game a draw?
        else if (this.player1Wins + this.player2Wins + this.draws == this.numGames) {
            this.matchStatus = MatchStatus.Complete.ordinal();
        }
    }

    public boolean wasShutout(String winningPlayerID) {
        if (this.player1ID.equals(winningPlayerID)) {
            return this.getPlayer2Wins() == 0;
        } else {
           return this.getPlayer1Wins() == 0;
        }
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
            this.matchStatus = MatchStatus.InProgress.ordinal();
            startTime = System.currentTimeMillis();
            endTime = startTime + timeLimit;
        }
    }

    public boolean playerIsInMatch(String playerID)
    {
        return (this.player1ID.equals(playerID) || this.player2ID.equals(playerID));
    }
}
