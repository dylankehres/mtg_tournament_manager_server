package com.djk.tournament_manager.dto;

import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDataDTO {
    @JsonProperty("winningPlayer")
    public Player winningPlayer;
    @JsonProperty("losingPlayer")
    public Player losingPlayer;
    @JsonProperty("gameID")
    public String gameID;
    @JsonProperty("match")
    public Match match;
    @JsonProperty("resultStatus")
    public int resultStatus;

    enum ResultStatus {ResultsPending, ResultsFinal, ResultsDisputed};

    public ResultDataDTO() {
        this.winningPlayer = new Player();
        this.losingPlayer = new Player();
        this.gameID = "-1";
        this.match = new Match();
        this.resultStatus = ResultStatus.ResultsPending.ordinal();
    }

    public ResultDataDTO(Player winningPlayerID, Player losingPlayerID, String gameID, Match match, int resultStatus) {
        this.winningPlayer = winningPlayerID;
        this.losingPlayer = losingPlayerID;
        this.gameID = gameID;
        this.match = match;
        this.resultStatus = resultStatus;
    }

    public static int getResultStatusPending() { return ResultStatus.ResultsPending.ordinal(); }

    public static int getResultStatusFinal() { return ResultStatus.ResultsFinal.ordinal(); }

    public static int getResultStatusDisputed() { return ResultStatus.ResultsDisputed.ordinal(); }
}
