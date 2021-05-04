package com.djk.tournament_manager.dto;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class PlayerInfoDTO {
    @JsonProperty("tournament") public Tournament tournament;
    @JsonProperty("player") public Player player;
    @JsonProperty("matches") public ArrayList<MatchDataDTO> matches;

    public PlayerInfoDTO() {
        this.tournament = new Tournament();
        this.player = new Player();
        this.matches = new ArrayList<>();
    }

    public PlayerInfoDTO(Tournament tournament, Player player, ArrayList<MatchDataDTO> matches) {

        this.tournament = tournament;
        this.player = player;
        this.matches = matches;
    }
}
