package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Match;

import java.util.List;
import java.util.UUID;

public interface MatchDao {
    Match insertMatch(String id, String tournamentID, int numGames, String player1, String player2, int tableNum);

    default Match insertMatch(String tournamentID, int numGames, String player1ID, String player2ID, int tableNum) {
        UUID id = UUID.randomUUID();
        return insertMatch(id.toString(), tournamentID, numGames, player1ID, player2ID, tableNum);
    }

    List<Match> selectAllMatches();

    List<Match> selectMatchesInTournament(String tournamentID);

    Match selectMatchById(String id);

    Match selectMatchByPlayerID(String playerId);

    void deleteMatchById(String id);

    Match updateMatch(Match match);

}
