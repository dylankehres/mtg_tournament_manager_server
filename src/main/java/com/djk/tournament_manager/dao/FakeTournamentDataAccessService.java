package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("fakeDao")
public class FakeTournamentDataAccessService implements TournamentDao {

    private static List<Tournament> DB = new ArrayList<>();

    @Override
    public int insertTournament(UUID id, Tournament tournament)
    {
        DB.add(new Tournament(id, tournament.getName()));
        return 1;
    }
}
