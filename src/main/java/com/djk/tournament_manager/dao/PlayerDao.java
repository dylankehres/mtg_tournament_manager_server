package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface PlayerDao {
    Player insertPlayer(String id, String tournamentID, String name, String roomCode, String format, String deckName, boolean bye);

    default Player insertPlayer(String tournamentID, String name, String roomCode, String format, String deckName, boolean bye) {
        UUID id = UUID.randomUUID();
        return insertPlayer(id.toString(), tournamentID, name, roomCode, format, deckName, bye);
    }

    ArrayList<Player> selectAllPlayers();

    ArrayList<Player> selectPlayersByTournament(String code);

    ArrayList<Player> selectPlayersByTournamentID (String tmtID);

    Player selectPlayerById(String id);

    void deletePlayerById(String id);

    Player updatePlayerById(Player player);
}
