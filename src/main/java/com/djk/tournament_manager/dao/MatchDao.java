package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface MatchDao {
    Match insertMatch(String id, Match newMatch);

    default Match insertMatch(Match newMatch) {
        UUID id = UUID.randomUUID();
        return insertMatch(id.toString(), new Match(id.toString(), newMatch.getTournamentID(), newMatch.getNumGames(),
                newMatch.getPlayer1ID(), newMatch.getPlayer2ID(), newMatch.getTableNum(), newMatch.getRoundNum()));
    }

    List<Match> selectAllMatches();

    List<Match> selectMatchesInTournament(String tournamentID);

    List<Match> selectMatchesInRound(String tournamentID, int roundNum);

    Match selectMatchById(String id);

    Match selectMatchByPlayerID(String playerId, int roundNum);

    ArrayList<Match> selectAllMatchesByPlayerID(String playerId);

    void deleteMatchById(String id);

    Match updateMatch(Match match);

}
