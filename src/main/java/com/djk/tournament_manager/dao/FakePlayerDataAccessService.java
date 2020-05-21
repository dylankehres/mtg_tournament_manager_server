package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fakePlayerDao")
public class FakePlayerDataAccessService implements PlayerDao {
    private static List<Player> DB = new ArrayList<>();

    @Override
    public String insertPlayer(String id, Player player) {
        DB.add(new Player(id, player.getTournamentID(), player.getName(), player.getRoomCode(), player.getFormat(), player.getDeckName()));
        return id;
    }

    @Override
    public List<Player> selectAllPlayers() {
        return DB;
    }

    @Override
    public List<Player> selectPlayersByTournament(String code) {
        List<Player> playersInTournament = new ArrayList<>();
        Iterator itr = DB.iterator();

        while(itr.hasNext())
        {
            Player p = (Player)itr.next();
            if(p.getRoomCode().equals(code))
            {
                playersInTournament.add(p);
            }
        }

        return playersInTournament;
    }

    @Override
    public Player selectPlayerById(String id) {
        Optional<Player> playerMaybe = DB.stream()
                .filter(player -> player.getID().equals(id))
                .findFirst();

        if(playerMaybe.isPresent()) {
            return playerMaybe.get();
        }

        return null;
    }

    @Override
    public void deletePlayerById(String id) {
        Player player = selectPlayerById(id);
        if (player != null)
        {
            DB.remove(player);
        }
    }

    @Override
    public void updatePlayerById(String id, Player update) {
        Player player = selectPlayerById(id);

        if(player != null) {
            int indexOfPlayerToUpdate = DB.indexOf(player);
            if(indexOfPlayerToUpdate >= 0){
                DB.set(indexOfPlayerToUpdate, new Player(id, update.getTournamentID(), update.getName(), update.getRoomCode(), update.getFormat(), update.getDeckName()));
            }
        }

//        return selectPlayerById(id)
//                .map(p -> {
//                    int indexOfPlayerToUpdate = DB.indexOf(p);
//                    if(indexOfPlayerToUpdate >= 0){
//                        DB.set(indexOfPlayerToUpdate, new Player(id, update.getTournamentID(), update.getName(), update.getRoomCode(), update.getFormat(), update.getDeckName()));
//                        return 1;
//                    }
//                    return 0;
//                }).orElse(0);
    }
}
