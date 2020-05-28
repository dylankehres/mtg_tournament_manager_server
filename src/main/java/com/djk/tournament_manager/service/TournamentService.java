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
    public TournamentService(@Qualifier("firebaseTournamentDao") TournamentDao tournamentDao, @Qualifier("firebasePlayerDao") PlayerDao playerDao, @Qualifier("firebaseMatchDao") MatchDao matchDao)
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
        return tournamentDao.selectTournamentById(id);
    }

    public void deleteTournament(String id) {
        tournamentDao.deleteTournamentById(id);
    }

    public void updateTournament(String id, Tournament tournament)
    {
        tournamentDao.updateTournamentById(id, tournament);
    }

    public String addPlayer(Player player) {
        Tournament tournament = tournamentDao.selectTournamentByCode(player.getRoomCode());

        if(!tournament.equals(null))
        {
            player.setTournamentID(tournament.getID());
            return playerDao.insertPlayer(player);
        }

        return null;
    }

    public Player getPlayerById(String id) {
        return playerDao.selectPlayerById(id);
    }

    public List<Player> getPlayersInTournament(String code){
        return playerDao.selectPlayersByTournament(code);
    }

    public void deletePlayer(String id) { playerDao.deletePlayerById(id); }

    public List<HashMap<String,Object>> generatePairings(String tournamentID)  {
        int numGames = 3;
        Tournament tournament = null;
        tournament = tournamentDao.selectTournamentById(tournamentID);


        if(!tournament.equals(null))
        {
            String code = tournament.getRoomCode();

            if (getMatchesByRoomCode(code).isEmpty())
            {
                List<Player> waitingPlayers = playerDao.selectPlayersByTournament(code);

                if (waitingPlayers.size() % 2 == 1)
                {
                    UUID id = UUID.randomUUID();
                    Player bye = new Player(id.toString(), tournament.getID(), "BYE", code, tournament.getFormat(), "");
                    playerDao.insertPlayer(id.toString(), bye);
                    waitingPlayers.add(bye);
                }

                Collections.shuffle(waitingPlayers);

                for (int i = 0; i < waitingPlayers.size(); i += 2) {
                 matchDao.insertMatch(tournament.getID(), numGames, waitingPlayers.get(i).getID(), waitingPlayers.get(i + 1).getID(), i+1 );
                }
            }

            return getMatchesByRoomCode(code);
        }

        return new ArrayList<>();
    }

    public List<HashMap<String,Object>> getMatchesByRoomCode(String code) {
        Tournament tournament = tournamentDao.selectTournamentByCode(code);
        List<HashMap<String,Object>> matchDataList = new ArrayList<>();

        if(tournament != null)
        {
            List<Match> matches = matchDao.selectMatchesInTournament(tournament.getID());

            for(Match match : matches) {
                Player p1 = playerDao.selectPlayerById(match.getPlayer1ID());
                Player p2 = playerDao.selectPlayerById(match.getPlayer2ID());
                HashMap<String,Object> matchData = new HashMap<>();

                matchData.put("player1", p1);
                matchData.put("player2", p2);
                matchData.put("match", match);
                matchDataList.add(matchData);
            }
        }

        return matchDataList;
    }

    public HashMap<String, Object> getMatchByPlayerID(String playerID)
    {
        HashMap<String, Object> matchAndPlayers = new HashMap<>();
        Match match = matchDao.selectMatchByPlayerID(playerID);
        Player p1 = new Player();
        Player p2 = new Player();

        if(match != null) {
            p1 = playerDao.selectPlayerById(match.getPlayer1ID());
            p2 = playerDao.selectPlayerById(match.getPlayer2ID());
        }
        else {
            match = new Match();
        }

        matchAndPlayers.put("match", match);
        matchAndPlayers.put("player1", p1);
        matchAndPlayers.put("player2", p2);

        return matchAndPlayers;
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
