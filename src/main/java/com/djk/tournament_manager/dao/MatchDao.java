package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchDao {
    String insertMatch(String id, String tournamentID, int numGames, Player player1, Player player2, int tableNum);

    default String insertMatch(String tournamentID, int numGames, Player player1, Player player2, int tableNum) {
        UUID id = UUID.randomUUID();
        return insertMatch(id.toString(), tournamentID, numGames, player1, player2, tableNum);
    }

    List<Match> selectAllMatches();

    List<Match> selectMatchesInTournament(String tournamentID);

    Optional<Match> selectMatchById(String id);

    Optional<Match> selectMatchByPlayerID(String playerId);

    int deleteMatchById(String id);

}
