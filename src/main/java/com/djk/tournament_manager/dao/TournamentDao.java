package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;

import java.util.*;
import java.util.concurrent.ExecutionException;

public interface TournamentDao {

    String insertTournament(String id, Tournament tournament);

    default String insertTournament(Tournament tournament) {
        UUID id = UUID.randomUUID();
        return insertTournament(id.toString(), new Tournament(id.toString(), tournament.getName(), tournament.getRoomCode(), tournament.getFormat()));
    }

    List<Tournament> selectAllTournaments();

    Tournament selectTournamentById(String id);

    Tournament selectTournamentByCode(String code);

    void deleteTournamentById(String id);

    void updateTournamentById(String id, Tournament tournament);

}
