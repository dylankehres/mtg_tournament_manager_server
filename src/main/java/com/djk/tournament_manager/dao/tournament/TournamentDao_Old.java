package com.djk.tournament_manager.dao.tournament;

import com.djk.tournament_manager.model.Tournament;

import java.util.*;

public interface TournamentDao_Old {

    Tournament insertTournament(String id, Tournament tournament);

    default Tournament insertTournament(Tournament tournament) {
        UUID id = UUID.randomUUID();
        return insertTournament(id.toString(), new Tournament(tournament.getName(), tournament.getRoomCode(), tournament.getFormat(), tournament.getNumRounds(), tournament.getNumGames()));
    }

    List<Tournament> selectAllTournaments();

    Tournament selectTournamentById(String id);

    Tournament selectTournamentByCode(String code);

    void deleteTournamentById(String id);

    Tournament updateTournamentById(Tournament tournament);

}
