package com.djk.tournament_manager.service;

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
    public TournamentService(@Qualifier("fakeTournamentDao") TournamentDao tournamentDao)
    {
        this.tournamentDao = tournamentDao;
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

    public UUID addPlayer(Player player){
        return tournamentDao.addPlayer(player.getRoomCode(), player);
    }

    public List<Player> getPlayersInTournament(String code){
        return tournamentDao.selectPlayersInTournament(code);
    }

}
