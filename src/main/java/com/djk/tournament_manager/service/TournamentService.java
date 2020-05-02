package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.PlayerDao;
import com.djk.tournament_manager.dao.TournamentDao;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@Service
public class TournamentService {

    private final TournamentDao tournamentDao;
    private final PlayerDao playerDao;

//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
////                registry.addMapping("/api/v1/tournament").allowedOrigins("http://localhost:3000");
//                registry.addMapping("/").allowedOrigins("*");
//            }
//        };
//    }

    @Autowired
    public TournamentService(@Qualifier("fakeTournamentDao") TournamentDao tournamentDao, @Qualifier("fakePlayerDao") PlayerDao playerDao)
    {
        this.tournamentDao = tournamentDao;
        this.playerDao = playerDao;
    }

    public UUID addTournament(Tournament tournament)
    {
        return tournamentDao.insertTournament(tournament);
    }

    public List<Tournament> getAllTournaments()
    {
         return tournamentDao.selectAllTournaments();
    }

    public Optional<Tournament> getTournamentById(UUID id)
    {
        return tournamentDao.selectTournamentById(id);
    }

    public int deleteTournament(UUID id) {
        return tournamentDao.deleteTournamentById(id);
    }

    public int updateTournament(UUID id, Tournament tournament)
    {
        return tournamentDao.updateTournamentById(id, tournament);
    }

//    public UUID addPlayer(Player player){
//        return tournamentDao.addPlayer(player.getRoomCode(), player);
//    }
    public UUID addPlayer(Player player){
        Optional<Tournament> tournamentMaybe = tournamentDao.selectTournamentByCode(player.getRoomCode());

        if(tournamentMaybe.isPresent())
        {
            player.setTournamentID(tournamentMaybe.get().getID());
            return playerDao.insertPlayer(player);
        }

        return null;
    }

    public Optional<Player> getPlayerById(UUID id)
    {
        return playerDao.selectPlayerById(id);
    }

    public List<Player> getPlayersInTournament(String code){
        return playerDao.selectPlayersByTournament(code);
    }

    public int deletePlayer(UUID id) { return playerDao.deletePlayerById(id); }

}
