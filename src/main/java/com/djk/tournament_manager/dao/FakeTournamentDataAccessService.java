package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fakeTournamentDao")
public class FakeTournamentDataAccessService implements TournamentDao {

    private static List<Tournament> DB = new ArrayList<>();

    @Override
    public UUID insertTournament(UUID id, Tournament tournament)
    {
        if(DB.stream().anyMatch(t -> t.getRoomCode().equals(tournament.getRoomCode()))) {
            return null;
        }

        DB.add(new Tournament(id, tournament.getName(), tournament.getRoomCode(), tournament.getFormat()));
        return id;
    }

    @Override
    public List<Tournament> selectAllTournaments() {
        return DB;
    }

    @Override
    public Optional<Tournament> selectTournamentById(UUID id) {
        return DB.stream()
                .filter(tournament -> tournament.getID().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Tournament> selectTournamentByCode(String code) {
        return DB.stream()
                .filter(tournament -> tournament.getRoomCode().equals(code))
                .findFirst();
    }

    @Override
    public int deleteTournamentById(UUID id) {
        Optional<Tournament> tournamentMaybe = selectTournamentById(id);
        if (tournamentMaybe.isPresent())
        {
            DB.remove(tournamentMaybe.get());
            return 1;
        }
        return 0;
    }

    @Override
    public int updateTournamentById(UUID id, Tournament update) {
        return selectTournamentById(id)
                .map(t -> {
                    int indexOfTournamentToUpdate = DB.indexOf(t);
                    if(indexOfTournamentToUpdate >= 0){
                        DB.set(indexOfTournamentToUpdate, new Tournament(id, update.getName(), update.getRoomCode(), update.getFormat()));
                        return 1;
                    }
                    return 0;
                }).orElse(0);
    }

//    @Override
//    public UUID addPlayer(String code, Player player)
//    {
//        Optional<Tournament> tournamentMaybe = selectTournamentByCode(code);
//        if (tournamentMaybe.isPresent())
//        {
//            Player newPlayer = new Player(UUID.randomUUID(), tournamentMaybe.get().getID(), player.getName(), player.getRoomCode(), player.getFormat(), player.getDeckName());
//            tournamentMaybe.get().addPlayer(newPlayer);
//            return newPlayer.getID();
//        }
//        return null;
//    }

//    @Override
//    public Optional<Player> selectPlayerById(UUID id) {
//        return DB.stream()
//                .filter(tournament -> tournament.getPlayerList().contains(id))
//                .findFirst();

//        return DB.stream().forEach(Tournament::getPlayerList().stream()
//                .filter(player -> player.getID().equals(id))
//                .findFirst());
//    }

    @Override
    public List<Player> selectPlayersInTournament(String code) {
//        Optional<Tournament> tournamentMaybe = selectTournamentByCode(code);
//        if (tournamentMaybe.isPresent())
//        {
//            return tournamentMaybe.get().getPlayerList();
//        }
        return new ArrayList<>();
    }


}
