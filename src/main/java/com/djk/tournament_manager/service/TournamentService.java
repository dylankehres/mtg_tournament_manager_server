package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.MatchDao;
import com.djk.tournament_manager.dao.PlayerDao;
import com.djk.tournament_manager.dao.TournamentDao;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class TournamentService {

    private final TournamentDao tournamentDao;
    private final PlayerDao playerDao;
    private final MatchDao matchDao;

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
//    public TournamentService(@Qualifier("fakeTournamentDao") TournamentDao tournamentDao, @Qualifier("fakePlayerDao") PlayerDao playerDao, @Qualifier("fakeMatchDao") MatchDao matchDao)
    public TournamentService(@Qualifier("firebaseTournamentDao") TournamentDao tournamentDao, @Qualifier("fakePlayerDao") PlayerDao playerDao, @Qualifier("fakeMatchDao") MatchDao matchDao)
    {
        this.tournamentDao = tournamentDao;
        this.playerDao = playerDao;
        this.matchDao = matchDao;
    }

    public String addTournament(Tournament tournament)
    {
        return tournamentDao.insertTournament(tournament);
    }

    public List<Tournament> getAllTournaments()
    {
         return tournamentDao.selectAllTournaments();
    }

    public Tournament getTournamentById(String id)
    {
        try {
            return tournamentDao.selectTournamentById(id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int deleteTournament(String id) {
        return tournamentDao.deleteTournamentById(id);
    }

    public String updateTournament(String id, Tournament tournament)
    {
        return tournamentDao.updateTournamentById(id, tournament);
    }

    public String addPlayer(Player player){
        Tournament tournament = tournamentDao.selectTournamentByCode(player.getRoomCode());

        if(!tournament.equals(null))
        {
            player.setTournamentID(tournament.getID());
            return playerDao.insertPlayer(player);
        }

        return null;
    }

    public Optional<Player> getPlayerById(String id)
    {
        return playerDao.selectPlayerById(id);
    }

    public List<Player> getPlayersInTournament(String code){
        return playerDao.selectPlayersByTournament(code);
    }

    public int deletePlayer(String id) { return playerDao.deletePlayerById(id); }

    public List<Match> generatePairings(String tournamentID)
    {
        int numGames = 3;
        Tournament tournament = null;
        try {
            tournament = tournamentDao.selectTournamentById(tournamentID);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(!tournament.equals(null))
        {
            String code = tournament.getRoomCode();

            if (getMatchesByRoomCode(code).isEmpty())
            {
                List<Player> waitingPlayers = playerDao.selectPlayersByTournament(code);

                if (waitingPlayers.size() % 2 == 1)
                {
                    UUID id = UUID.randomUUID();
                    waitingPlayers.add(new Player(id.toString(), tournament.getID(), "BYE", code, tournament.getFormat(), ""));
                }

                Collections.shuffle(waitingPlayers);

                for (int i = 0; i < waitingPlayers.size(); i += 2) {
                 matchDao.insertMatch(tournament.getID(), numGames, waitingPlayers.get(i), waitingPlayers.get(i + 1), i);
                }
            }

            return getMatchesByRoomCode(code);
        }

        return new ArrayList<>();
    }

    public List<Match> getMatchesByRoomCode(String code)
    {
        Tournament tournament = tournamentDao.selectTournamentByCode(code);
        if(!tournament.equals(null))
        {
            return matchDao.selectMatchesInTournament(tournament.getID());
        }

        return new ArrayList<>();

    }

    public Optional<Match> getMatchByPlayerID(String playerID)
    {
        return matchDao.selectMatchByPlayerID(playerID);
    }

    public void deleteMatchByTournamentID(String tournamentID)
    {
        List<Match> matchesToDelete = matchDao.selectMatchesInTournament(tournamentID);

        Iterator itr = matchesToDelete.iterator();

        while(itr.hasNext())
        {
            Match m = (Match)itr.next();
            matchDao.deleteMatchById(m.getID());
        }
    }

}
