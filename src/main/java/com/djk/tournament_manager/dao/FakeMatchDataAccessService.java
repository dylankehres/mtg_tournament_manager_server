package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fakeMatchDao")
public class FakeMatchDataAccessService {
//public class FakeMatchDataAccessService implements MatchDao {
    private static List<Match> DB = new ArrayList<>();

//    @Override
    public String insertMatch(String id, String tournamentID, int numGames, String player1ID, String player2ID, int tableNum, int roundNum) {
        DB.add(new Match(id, tournamentID, numGames, player1ID, player2ID, tableNum, roundNum));
        return id;
    }

//    @Override
    public List<Match> selectAllMatches() {
        return DB;
    }

//    @Override
    public List<Match> selectMatchesInTournament(String tournamentID) {
        List<Match> matchesInTournament = new ArrayList<>();
        Iterator itr = DB.iterator();

        while(itr.hasNext())
        {
            Match m = (Match)itr.next();
            if(m.getTournamentID().equals(tournamentID))
            {
                matchesInTournament.add(m);
            }
        }

        return matchesInTournament;
    }

//    @Override
    public Optional<Match> selectMatchById(String id) {
        return DB.stream()
                .filter(match -> match.getID().equals(id))
                .findFirst();
    }

//    @Override
    public Optional<Match> selectMatchByPlayerID(String playerId) {
        return DB.stream()
                .filter(match -> match.playerIsInMatch(playerId))
                .findFirst();
    }

//    @Override
    public int deleteMatchById(String id) {
        Optional<Match> playerMaybe = selectMatchById(id);
        if (playerMaybe.isPresent())
        {
            DB.remove(playerMaybe.get());
            return 1;
        }
        return 0;
    }
}
