package com.djk.tournament_manager.dao.match;

import com.djk.tournament_manager.model.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface MatchDaoOld {
    Match insert(String id, Match newMatch);

    default Match insert(Match newMatch) {
        UUID id = UUID.randomUUID();
        return insert(id.toString(), new Match(id.toString(), newMatch.getTournamentID(), newMatch.getNumGames(),
                newMatch.getPlayer1ID(), newMatch.getPlayer2ID(), newMatch.getTableNum(), newMatch.getRoundNum()));
    }

    List<Match> selectAll();

    List<Match> selectMatchesInTournament(String tournamentID);

    List<Match> selectMatchesInRound(String tournamentID, int roundNum);

    Match selectById(String id);

    Match selectMatchByPlayerID(String playerId, int roundNum);

    ArrayList<Match> selectAllMatchesByPlayerID(String playerId);

    void deleteById(String id);

    Match update(Match match);
}
