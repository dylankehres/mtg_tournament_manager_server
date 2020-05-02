package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;

import java.util.*;

public interface TournamentDao {

    UUID insertTournament(UUID id, Tournament tournament);

    default UUID insertTournament(Tournament tournament) {
        UUID id = UUID.randomUUID();
        return insertTournament(id, tournament);
    }

    List<Tournament> selectAllTournaments();

    Optional<Tournament> selectTournamentById(UUID id);

    Optional<Tournament> selectTournamentByCode(String code);

    int deleteTournamentById(UUID id);

    int updateTournamentById(UUID id, Tournament tournament);

//    UUID addPlayer(String roomCode, Player player);

//    Optional<Player> selectPlayerById(UUID id);

    List<Player> selectPlayersInTournament(String code);
}
