package com.djk.tournament_manager.dto;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class PlayerHubDTO {
    @JsonProperty("tournament") public Tournament tournament;
    @JsonProperty("playerList") public ArrayList<Player> playerList;
    @JsonProperty("pairings") public ArrayList<MatchDataDTO> pairings;
    @JsonProperty("matchData") public MatchDataDTO matchData;
    @JsonProperty("currPlayer") public Player currPlayer;

    public PlayerHubDTO() {
        this.tournament = new Tournament();
        this.playerList = new ArrayList<>();
        this.pairings = new ArrayList<>();
        this.matchData = new MatchDataDTO();
        this.currPlayer = new Player();
    }

    public PlayerHubDTO(Tournament tournament, ArrayList<Player> playerList, ArrayList<MatchDataDTO> pairings, MatchDataDTO matchData, Player currPlayer) {

        this.tournament = tournament;
        this.playerList = playerList;
        this.pairings = pairings;
        this.matchData = matchData;
        this.currPlayer = currPlayer;
    }

}
