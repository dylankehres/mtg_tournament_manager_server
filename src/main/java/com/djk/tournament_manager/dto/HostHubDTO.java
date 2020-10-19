package com.djk.tournament_manager.dto;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class HostHubDTO {
    @JsonProperty("tournament") public Tournament tournament;
    @JsonProperty("pairings") public ArrayList<MatchDataDTO> pairings;
    @JsonProperty("playerList") public ArrayList<Player> playerList;

    public HostHubDTO() {
        this.tournament = new Tournament();
        this.pairings = new ArrayList<>();
        this.playerList = new ArrayList<>();
    }

    public HostHubDTO(Tournament tournament, ArrayList<MatchDataDTO> pairings, ArrayList<Player> playerList) {
        this.tournament = tournament;
        this.pairings = pairings;
        this.playerList = playerList;
    }
}
