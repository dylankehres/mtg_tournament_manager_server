package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.GameDao;
import com.djk.tournament_manager.dao.MatchDao;
import com.djk.tournament_manager.dao.PlayerDao;
import com.djk.tournament_manager.dao.TournamentDao;
import com.djk.tournament_manager.dto.MatchDataDTO;
import com.djk.tournament_manager.model.Game;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TournamentService {

    private final TournamentDao tournamentDao;
    private final PlayerDao playerDao;
    private final MatchDao matchDao;
    private final GameDao gameDao;

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
    public TournamentService(@Qualifier("firebaseTournamentDao") TournamentDao tournamentDao, @Qualifier("firebasePlayerDao") PlayerDao playerDao, @Qualifier("firebaseMatchDao") MatchDao matchDao, @Qualifier("firebaseGameDao") GameDao gameDao)
    {
        this.tournamentDao = tournamentDao;
        this.playerDao = playerDao;
        this.matchDao = matchDao;
        this.gameDao = gameDao;
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

    public Player addPlayer(Player player) {
        Tournament tournament = tournamentDao.selectTournamentByCode(player.getRoomCode());

        if(!tournament.equals(null))
        {
            player.setTournamentID(tournament.getID());
            return playerDao.insertPlayer(player.getTournamentID(), player.getName(), player.getRoomCode(), player.getFormat(), player.getDeckName());
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

    public List<MatchDataDTO> generatePairings(String tournamentID)  {
        int numGames = 3;
        Tournament tournament = null;
        tournament = tournamentDao.selectTournamentById(tournamentID);


        if(!tournament.equals(null))
        {
            String code = tournament.getRoomCode();
            Match newMatch = new Match();
            Game newGame = new Game();

            if (getMatchesByRoomCode(code).isEmpty())
            {
                List<Player> waitingPlayers = playerDao.selectPlayersByTournament(code);

                if (waitingPlayers.size() % 2 == 1)
                {
                    Player bye = playerDao.insertPlayer(tournament.getID(), "BYE", code, tournament.getFormat(), "");
                    waitingPlayers.add(bye);
                }

                Collections.shuffle(waitingPlayers);

                for (int i = 0; i < waitingPlayers.size(); i += 2) {
                    newMatch = matchDao.insertMatch(tournament.getID(), numGames, waitingPlayers.get(i).getID(), waitingPlayers.get(i + 1).getID(), i+1);
                    newGame = gameDao.insertGame(newMatch.getID());
                    newMatch.addNewActiveGameKey(newGame.getID());
                    matchDao.updateMatch(newMatch);
                }
            }

            return getMatchesByRoomCode(code);
        }

        return new ArrayList<>();
    }

    public List<MatchDataDTO> getMatchesByRoomCode(String code) {
        Tournament tournament = tournamentDao.selectTournamentByCode(code);
        List<MatchDataDTO> matchDataList = new ArrayList<>();

        if(tournament != null)
        {
            List<Match> matches = matchDao.selectMatchesInTournament(tournament.getID());

            for(Match match : matches) {
                Player p1 = playerDao.selectPlayerById(match.getPlayer1ID());
                Player p2 = playerDao.selectPlayerById(match.getPlayer2ID());

                matchDataList.add(new MatchDataDTO(p1, p2, match));
            }
        }

        return matchDataList;
    }

    public MatchDataDTO getMatchByPlayerID(String playerID)
    {
        Match match = matchDao.selectMatchByPlayerID(playerID);
        Player p1 = new Player();
        Player p2 = new Player();
        Game game = new Game();
        List<Game> gameList = new ArrayList<>();
        MatchDataDTO matchData = new MatchDataDTO();

        if(match != null) {
            p1 = playerDao.selectPlayerById(match.getPlayer1ID());
            p2 = playerDao.selectPlayerById(match.getPlayer2ID());
            gameList = gameDao.selectGamesInMatch(match.getID());
        }
        else {
            match = new Match();
        }

        matchData = new MatchDataDTO(p1, p2, match, gameList);
        return matchData;
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

    public Match incrementPlayerWin(String playerID) {
        Match match = matchDao.selectMatchByPlayerID(playerID);
        match.addPlayerWin(playerID);

        return match;
    }

    public MatchDataDTO reportGameResults(String votingPlayerID, String winningPlayerID) {
        // Query for the corresponding match and game
        Match match = matchDao.selectMatchByPlayerID(votingPlayerID);
        Game game = gameDao.selectGameById(match.activeGameID());

        // Report results
        int resultStatus = game.votePlayerWin(match, votingPlayerID, winningPlayerID);

        if(resultStatus == Game.getResultStatusFinal())
        {
            // Results are in start a new game
            game.setIsActive(false);
            Game newGame = gameDao.insertGame(match.getID());
            newGame.setIsActive(true);
            match.addNewActiveGameKey(newGame.getID());
        }

        matchDao.updateMatch(match);
        gameDao.updateGame(game);

        // Prepare MatchDatDTO
        List<Game> gameList = gameDao.selectGamesInMatch(match.getID());
        Player p1 = playerDao.selectPlayerById(match.getPlayer1ID());
        Player p2 = playerDao.selectPlayerById(match.getPlayer2ID());
        MatchDataDTO matchData = new MatchDataDTO(p1, p2, match, gameList);

        return matchData;
    }
}
