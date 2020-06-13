package com.djk.tournament_manager.model;

import com.djk.tournament_manager.dto.ResultDataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;

public class Game {
    @JsonProperty("id") private String id;
    @JsonProperty("matchID") private String matchID;
    @JsonProperty("player1Voted") private boolean player1Voted;
    @JsonProperty("player2Voted") private boolean player2Voted;
    @JsonProperty("player1Wins") private int player1Wins;
    @JsonProperty("player2Wins") private int player2Wins;
    @JsonProperty("draw") private int draw;
    @JsonProperty("active") private boolean active;

    enum GameResultVotes {
        WIN, LOSS, DRAW
    }

    public Game() {
        this.id = "";
        this.matchID = "";
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.draw = 0;
        this.player1Voted = false;
        this.player2Voted = false;
        this.active = false;
    }

    public Game(String id, String matchID) {
        this.id = id;
        this.matchID = matchID;
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.draw = 0;
        this.player1Voted = false;
        this.player2Voted = false;
        this.active = false;
    }

    public String getID() {
        return this.id;
    }

    public String getMatchID() {
        return this.matchID;
    }

    public boolean getPlayer1Voted() { return this.player1Voted; }

    public boolean getPlayer2Voted() { return this.player2Voted; }

    public int getPlayer1Wins() { return this.player1Wins; }

    public int getPlayer2Wins() { return this.player2Wins; }

    public int getDraw() { return this.draw; }

    public boolean isActive() { return this.active; }

    public ResultDataDTO votePlayerWin(Match match, String votingPlayerID, String winningPlayerID) {
        boolean playerCanVote = false;

        // Check if this player has already voted for a winner
        if (match.getPlayer1ID().equals(votingPlayerID)) {
            if(this.player1Voted) {
                playerCanVote = false;
            }
            else {
                playerCanVote = true;
                this.player1Voted = true;
            }
        }
        else if (match.getPlayer2ID().equals(votingPlayerID)) {
            if(this.player2Voted) {
                playerCanVote = false;
            }
            else {
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
            } else {
                System.err.println("ERROR: Unable to increment player wins");
            }
        }

        // Are all the votes in? Return the results
        if (player1Voted && player2Voted) {
            if(player1Wins == 2 || player2Wins == 2) {
                String winningPlayer = player1Wins == 2 ? match.getPlayer1ID() : match.getPlayer2ID();
                String losingPlayer = winningPlayer.equals(match.getPlayer1ID()) ? match.getPlayer2ID() : match.getPlayer1ID();

                match.addPlayerWin(winningPlayer);

                return new ResultDataDTO(new Player(winningPlayer), new Player(losingPlayer), this.id, match, ResultDataDTO.getResultStatusFinal());
            }
            else {
                // There is a disagreement that needs settled
                return new ResultDataDTO(new Player(), new Player(), this.id, match, ResultDataDTO.getResultStatusDisputed());
            }
        }

        // Still waiting on results return the old match
        return new ResultDataDTO(new Player(), new Player(), this.id, match, ResultDataDTO.getResultStatusPending());
    }
}
