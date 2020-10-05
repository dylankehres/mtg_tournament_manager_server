package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface PlayerDao {
    Player insertPlayer(String id, String tournamentID, String name, String roomCode, String format, String deckName);

    default Player insertPlayer(String tournamentID, String name, String roomCode, String format, String deckName) {
        UUID id = UUID.randomUUID();
        return insertPlayer(id.toString(), tournamentID, name, roomCode, format, deckName);
    }

    List<Player> selectAllPlayers();

    List<Player> selectPlayersByTournament(String code);

    List<Player> selectPlayersByTournamentID (String tmtID);

    Player selectPlayerById(String id);

    void deletePlayerById(String id);

    Player updatePlayerById(Player player);
}
