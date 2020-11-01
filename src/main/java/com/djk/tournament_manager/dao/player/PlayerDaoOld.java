package com.djk.tournament_manager.dao.player;

import com.djk.tournament_manager.model.Player;

import java.util.ArrayList;
import java.util.UUID;

public interface PlayerDaoOld {
    Player insert(String id, Player player);

    default Player insert(Player player) {
        UUID id = UUID.randomUUID();
        player.setID(id.toString());
        return insert(id.toString(), player);
    }

    ArrayList<Player> selectAll();

    ArrayList<Player> selectPlayersByTournamentCode(String code);

    ArrayList<Player> selectPlayersByTournamentID (String tmtID);

    Player selectById(String id);

    void deleteById(String id);

    Player update(Player player);
}
