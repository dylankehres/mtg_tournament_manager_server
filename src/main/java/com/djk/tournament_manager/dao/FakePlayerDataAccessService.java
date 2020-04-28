package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakePlayerDao")
public class FakePlayerDataAccessService implements PlayerDao {
    private static List<Player> DB = new ArrayList<>();

    @Override
    public UUID insertPlayer(UUID id, Player player) {
        DB.add(new Player(id, player.getName(), player.getRoomCode(), player.getFormat(), player.getDeckName()));
        return id;
    }

    @Override
    public List<Player> selectAllPlayers() {
        return DB;
    }

    @Override
    public Optional<Player> selectPlayerById(UUID id) {
        return DB.stream()
                .filter(player -> player.getID().equals(id))
                .findFirst();
    }

    @Override
    public int deletePlayerById(UUID id) {
        Optional<Player> playerMaybe = selectPlayerById(id);
        if (playerMaybe.isPresent())
        {
            DB.remove(playerMaybe.get());
            return 1;
        }
        return 0;
    }

    @Override
    public int updatePlayerById(UUID id, Player update) {
        return selectPlayerById(id)
                .map(p -> {
                    int indexOfPlayerToUpdate = DB.indexOf(p);
                    if(indexOfPlayerToUpdate >= 0){
                        DB.set(indexOfPlayerToUpdate, new Player(id, update.getName(), update.getRoomCode(), update.getFormat(), update.getDeckName()));
                        return 1;
                    }
                    return 0;
                }).orElse(0);
    }
}
