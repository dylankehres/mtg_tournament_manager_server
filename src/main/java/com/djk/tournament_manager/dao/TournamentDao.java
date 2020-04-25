package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Tournament;

import java.util.*;

public interface TournamentDao {

    int insertTournament(UUID id, Tournament tournament);

    default int insertTournament(Tournament tournament) {
        UUID id = UUID.randomUUID();
        return insertTournament(id, tournament);
    }

    List<Tournament> selectAllTournaments();

    Optional<Tournament> selectTournamentById(UUID id);

    int deleteTournamentById(UUID id);

    int updateTournamentById(UUID id, Tournament tournament);
}
