package com.djk.tournament_manager.dao.match;

import com.djk.tournament_manager.model.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface MatchDAO {
    Match insert(String id, Match newMatch);

    Match insert(Match newMatch);

    ArrayList<Match> selectAll();

    ArrayList<Match> selectMatchesInTournament(String tournamentID);

    ArrayList<Match> selectMatchesInRound(String tournamentID, int roundNum);

    Match selectById(String id);

    Match selectMatchByPlayerID(String playerId, int roundNum);

    ArrayList<Match> selectAllMatchesByPlayerID(String playerId);

    void deleteById(String id);

    Match update(Match match);
}
