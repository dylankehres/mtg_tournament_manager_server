package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerDao {
    UUID insertPlayer(UUID id, Player player);

    default UUID insertPlayer(Player player) {
        UUID id = UUID.randomUUID();
        return insertPlayer(id, player);
    }

    List<Player> selectAllPlayers();

    List<Player> selectPlayersByTournament(String code);

    Optional<Player> selectPlayerById(UUID id);

    int deletePlayerById(UUID id);

    int updatePlayerById(UUID id, Player player);
}
