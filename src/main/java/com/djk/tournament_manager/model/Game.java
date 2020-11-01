package com.djk.tournament_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game extends BaseModel{
    @JsonProperty("matchID") private final String matchID;
    @JsonProperty("tournamentID") private final String tournamentID;
    @JsonProperty("gameNum") private int gameNum;
    @JsonProperty("player1Voted") private boolean player1Voted;
    @JsonProperty("player2Voted") private boolean player2Voted;
    @JsonProperty("player1Wins") private int player1Wins;
    @JsonProperty("player2Wins") private int player2Wins;
    @JsonProperty("player1Ready") private boolean player1Ready;
    @JsonProperty("player2Ready") private boolean player2Ready;
    @JsonProperty("draw") private int draw;
    @JsonProperty("resultStatus") private int resultStatus;
    @JsonProperty("winningPlayerID") private String winningPlayerID;

    enum ResultStatus {
        NoResults, ResultsPending, ResultsFinal, ResultsDisputed
    }

    public Game() {
        super();

        this.matchID = "";
        this.tournamentID = "";
        this.gameNum = 0;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.player1Ready = false;
        this.player2Ready = false;
        this.draw = 0;
        this.player1Voted = false;
        this.player2Voted = false;
        this.resultStatus = ResultStatus.NoResults.ordinal();
        this.winningPlayerID = "-1";
    }

    public Game(String matchID, String tournamentID, int gameNum) {
        super();

        this.matchID = matchID;
        this.tournamentID = tournamentID;
        this.gameNum = gameNum;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.player1Ready = false;
        this.player2Ready = false;
        this.draw = 0;
        this.player1Voted = false;
        this.player2Voted = false;
        this.resultStatus = ResultStatus.NoResults.ordinal();
        this.winningPlayerID = "-1";
    }

    public String getMatchID() {
        return this.matchID;
    }

    public String getTournamentID() { return this.tournamentID; }

    public int getGameNum() { return this.gameNum; }
    public void setGameNum(int gameNum) { this.gameNum = gameNum; }

    public boolean getPlayer1Voted() { return this.player1Voted; }

    public boolean getPlayer2Voted() { return this.player2Voted; }

    public int getPlayer1Wins() { return this.player1Wins; }

    public int getPlayer2Wins() { return this.player2Wins; }

    public String getWinningPlayerID() { return this.winningPlayerID; }

    public int getDraw() { return this.draw; }

    public int getResultStatus() { return this.resultStatus; }

    public int votePlayerWin(Match match, String votingPlayerID, String winningPlayerID) {
        boolean playerCanVote = false;

        // Check if this player has already voted for a winner
        if (match.getPlayer1ID().equals(votingPlayerID)) {
            if(!this.player1Voted) {
                playerCanVote = true;
                this.player1Voted = true;
            }
        }
        else if (match.getPlayer2ID().equals(votingPlayerID)) {
            if(!this.player2Voted) {
                playerCanVote = true;
                this.player2Voted = true;
            }
        }
        else {
            System.err.println("ERROR: Unable to tally player votes");
        }

        // If the player has not already voted, tally their vote
        if(playerCanVote) {
            if (match.getPlayer1ID().equals(winningPlayerID)) {
                this.player1Wins++;
            } else if (match.getPlayer2ID().equals(winningPlayerID)) {
                this.player2Wins++;
            } else if ("Draw".equals(winningPlayerID)){
                this.draw++;
            }
            else {
                System.err.println("ERROR: Unable to increment player wins");
            }
        }

        // Are all the votes in? Return the results
        if (player1Voted && player2Voted && playerCanVote) {
            if(player1Wins == 2 || player2Wins == 2) {
                String winningPlayer = player1Wins == 2 ? match.getPlayer1ID() : match.getPlayer2ID();
//                String losingPlayer = winningPlayer.equals(match.getPlayer1ID()) ? match.getPlayer2ID() : match.getPlayer1ID();

                match.addPlayerWin(winningPlayer);
                this.winningPlayerID = winningPlayer;
                this.resultStatus = Game.getResultStatusFinal();
            }
            else if(draw > 0) {
                match.didDraw();
                this.winningPlayerID = "Draw";
                this.resultStatus = Game.getResultStatusFinal();
            }
            else {
                // There is a disagreement that needs settled
                this.resultStatus =  Game.getResultStatusDisputed();
                this.player1Voted = false;
                this.player2Voted = false;
                this.player1Wins = 0;
                this.player2Wins = 0;
            }
        }
        else {
            // Still waiting on results return the old match
            this.resultStatus =  Game.getResultStatusPending();
        }

        return this.resultStatus;
    }

    public static int getResultStatusPending() { return ResultStatus.ResultsPending.ordinal(); }

    public static int getResultStatusFinal() { return ResultStatus.ResultsFinal.ordinal(); }

    public static int getResultStatusDisputed() { return ResultStatus.ResultsDisputed.ordinal(); }
}
