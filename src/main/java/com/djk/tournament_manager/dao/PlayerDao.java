package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface PlayerDao {
    String insertPlayer(String id, Player player);

    default String insertPlayer(Player player) {
        UUID id = UUID.randomUUID();
        return insertPlayer(id.toString(), new Player(id.toString(), player.getTournamentID(), player.getName(), player.getRoomCode(), player.getFormat(), player.getDeckName()));
    }

    List<Player> selectAllPlayers();

    List<Player> selectPlayersByTournament(String code);

    Player selectPlayerById(String id) throws ExecutionException, InterruptedException;

    void deletePlayerById(String id);

    void updatePlayerById(String id, Player player);
}
