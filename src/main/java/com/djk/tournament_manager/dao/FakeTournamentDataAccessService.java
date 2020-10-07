package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Tournament;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("fakeTournamentDao")
public class FakeTournamentDataAccessService {
//public class FakeTournamentDataAccessService implements TournamentDao {

    private static List<Tournament> DB = new ArrayList<>();

//    @Override
    public String insertTournament(String id, Tournament tournament)
    {
        if(DB.stream().anyMatch(t -> t.getRoomCode().equals(tournament.getRoomCode()))) {
            return null;
        }

        DB.add(new Tournament(id, tournament.getName(), tournament.getRoomCode(), tournament.getFormat(), tournament.getNumRounds(), tournament.getNumGames()));
        return id;
    }

//    @Override
    public List<Tournament> selectAllTournaments() {
        return DB;
    }

//    @Override
    public Tournament selectTournamentById(String id) {
        Optional<Tournament> tournamentMaybe = DB.stream()
                .filter(tournament -> tournament.getID().equals(id))
                .findFirst();

        if(tournamentMaybe.isPresent()){
            return  tournamentMaybe.get();
        }

        return null;
    }

//    @Override
    public Tournament selectTournamentByCode(String code) {
        Optional<Tournament> tournamentMaybe = DB.stream()
                .filter(tournament -> tournament.getRoomCode().equals(code))
                .findFirst();

        if(tournamentMaybe.isPresent()){
            return  tournamentMaybe.get();
        }

        return null;
    }

//    @Override
    public void deleteTournamentById(String id) {
        Tournament tournament = selectTournamentById(id);
        if (!tournament.equals(null))
        {
            DB.remove(tournament);
        }
    }

//    @Override
    public void updateTournamentById(String id, Tournament update) {
        Tournament t = selectTournamentById(id);
        if (!t.equals(null))
        {
            int indexOfTournamentToUpdate = DB.indexOf(t);
            if(indexOfTournamentToUpdate >= 0){
                DB.set(indexOfTournamentToUpdate, new Tournament(id, update.getName(), update.getRoomCode(), update.getFormat(), update.getNumRounds(), update.getNumGames()));
            }
        }
    }
}
