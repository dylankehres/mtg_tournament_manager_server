package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fakeDao")
public class FakeTournamentDataAccessService implements TournamentDao {

    private static List<Tournament> DB = new ArrayList<>();

    @Override
    public int insertTournament(UUID id, Tournament tournament)
    {
        DB.add(new Tournament(id, tournament.getName()));
        return 1;
    }

    @Override
    public List<Tournament> selectAllTournaments() {
        return DB;
    }

    @Override
    public Optional<Tournament> selectTournamentById(UUID id) {
        return DB.stream()
                .filter(tournament -> tournament.getID().equals(id))
                .findFirst();
    }

    @Override
    public int deleteTournamentById(UUID id) {
        Optional<Tournament> tournamentMaybe = selectTournamentById(id);
        if (tournamentMaybe.isPresent())
        {
            DB.remove(tournamentMaybe.get());
            return 1;
        }
        return 0;
    }

    @Override
    public int updateTournamentById(UUID id, Tournament update) {
        return selectTournamentById(id)
                .map(t -> {
                    int indexOfTournamentToUpdate = DB.indexOf(t);
                    if(indexOfTournamentToUpdate >= 0){
                        DB.set(indexOfTournamentToUpdate, new Tournament(id, update.getName()));
                        return 1;
                    }
                    return 0;
                }).orElse(0);
    }
}
