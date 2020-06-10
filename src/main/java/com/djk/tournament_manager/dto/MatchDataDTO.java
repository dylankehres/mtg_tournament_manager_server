package com.djk.tournament_manager.dto;

import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchDataDTO {
    @JsonProperty("player1") public Player player1;
    @JsonProperty("player2") public Player player2;
    @JsonProperty("match") public Match match;


    public MatchDataDTO() {
        this.player1 = new Player();
        this.player2 = new Player();
        this.match = new Match();
    }

    public MatchDataDTO(Player player1, Player player2, Match match) {
        this.player1 = player1;
        this.player2 = player2;
        this.match = match;
    }
}
