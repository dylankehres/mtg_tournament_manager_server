package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchDao {
    UUID insertMatch(UUID id, UUID tournamentID, int numGames, Player player1, Player player2, int tableNum);

    default UUID insertMatch(UUID tournamentID, int numGames, Player player1, Player player2, int tableNum) {
        UUID id = UUID.randomUUID();
        return insertMatch(id, tournamentID, numGames, player1, player2, tableNum);
    }

    List<Match> selectAllMatches();

    List<Match> selectMatchesInTournament(UUID tournamentID);

    Optional<Match> selectMatchById(UUID id);

    Optional<Match> selectMatchByPlayerID(UUID playerId);

    int deleteMatchById(UUID id);

}
