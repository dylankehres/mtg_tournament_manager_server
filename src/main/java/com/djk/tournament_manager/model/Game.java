package com.djk.tournament_manager.model;

import com.djk.tournament_manager.dao.MatchDao;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Qualifier;

public class Game {
    @JsonProperty("id") private String id;
    @JsonProperty("matchID") private String matchID;
    @JsonProperty("player1Vote") private boolean player1Voted;
    @JsonProperty("player2Vote") private boolean player2Voted;
    @JsonProperty("player1Vote") private int player1Win;
    @JsonProperty("player2Vote") private int player2Win;
    @JsonProperty("draw") private int draw;

    @Qualifier("firebaseMatchDao") MatchDao matchDao;

    enum GameResultVotes {
        WIN, LOSS, DRAW
    }

    public Game() {
        this.id = "";
        this.matchID = "";
        this.player1Win = 0;
        this.player2Win = 0;
        this.draw = 0;
        this.player1Voted = false;
        this.player2Voted = false;
    }

    public Game(String id, String matchID) {
        this.id = id;
        this.matchID = matchID;
        this.player1Win = 0;
        this.player2Win = 0;
        this.draw = 0;
        this.player1Voted = false;
        this.player2Voted = false;

    }

    public String getID() {
        return this.id;
    }

    public String getMatchID() {
        return this.matchID;
    }

    public int getDraws() {
        return this.draw;
    }

    public Match votePlayerWin(Match match, String votingPlayerID, String winningPlayerID) {
        boolean playerCanVote = false;

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

        if(playerCanVote) {
            if (match.getPlayer1ID().equals(winningPlayerID)) {
                this.player1Win++;
            } else if (match.getPlayer2ID().equals(winningPlayerID)) {
                this.player2Win++;
            } else {
                System.err.println("ERROR: Unable to increment player wins");
            }
        }

        if (player1Voted && player2Voted) {
            if(player1Win == 2 || player2Win == 2) {
                String winningPlayer = player1Win == 2 ? match.getPlayer1ID() : match.getPlayer2ID();
                match.addPlayerWin(winningPlayer);
                return match;
            }
            else {
                // There is a disagreement that needs settled
                return new Match();
            }
        }

        return new Match();
    }
}
