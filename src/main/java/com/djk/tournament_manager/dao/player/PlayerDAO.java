package com.djk.tournament_manager.dao.player;

import com.djk.tournament_manager.model.Player;

import java.util.ArrayList;

public interface PlayerDAO {
    Player insert(String id, Player player);

    Player insert(Player player);

    ArrayList<Player> selectAll();

    ArrayList<Player> selectPlayersByTournamentCode(String code);

    ArrayList<Player> selectPlayersByTournamentID (String tmtID);

    Player selectById(String id);

    void deleteById(String id);

    Player update(Player player);
}
