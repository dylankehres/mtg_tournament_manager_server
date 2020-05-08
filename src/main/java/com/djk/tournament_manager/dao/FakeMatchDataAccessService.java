package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fakeMatchDao")
public class FakeMatchDataAccessService implements MatchDao {
    private static List<Match> DB = new ArrayList<>();

    @Override
    public UUID insertMatch(UUID id, UUID tournamentID, int numGames, Player player1, Player player2, int tableNum) {
        DB.add(new Match(id, tournamentID, numGames, player1, player2, tableNum));
        return id;
    }

    @Override
    public List<Match> selectAllMatches() {
        return DB;
    }

    @Override
    public List<Match> selectMatchesInTournament(UUID tournamentID) {
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

    @Override
    public Optional<Match> selectMatchById(UUID id) {
        return DB.stream()
                .filter(match -> match.getID().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Match> selectMatchByPlayerID(UUID playerId) {
        return DB.stream()
                .filter(match -> match.playerIsInMatch(playerId))
                .findFirst();
    }

    @Override
    public int deleteMatchById(UUID id) {
        Optional<Match> playerMaybe = selectMatchById(id);
        if (playerMaybe.isPresent())
        {
            DB.remove(playerMaybe.get());
            return 1;
        }
        return 0;
    }
}
