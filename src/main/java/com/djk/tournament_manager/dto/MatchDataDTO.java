package com.djk.tournament_manager.dto;

import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;

public class MatchDataDTO {
    public Player player1;
    public Player player2;
    public Match match;

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
