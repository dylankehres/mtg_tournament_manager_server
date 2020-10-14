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
        if(tournamentDao.selectTournamentByCode(tournament.getRoomCode()) != null) {
            return "-1";
        }

        Tournament newTournament = tournamentDao.insertTournament(tournament);
        return newTournament.getID();
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

    public void deleteTournamentData(String tmtID) {
        // Query for all objects that are children of this tournament
        Tournament tournament = tournamentDao.selectTournamentById(tmtID);
        List<Match> matches = matchDao.selectMatchesInTournament(tmtID);
        List<Game> games = gameDao.selectGamesInTournament(tmtID);
        List<Player> players = playerDao.selectPlayersByTournamentID(tmtID);

        // Delete all tournament data
        for(Player player : players) {
            playerDao.deletePlayerById(player.getID());
        }

        for(Game game : games) {
            gameDao.deleteGameById(game.getID());
        }

        for(Match match : matches) {
            matchDao.deleteMatchById(match.getID());
        }

        tournamentDao.deleteTournamentById(tmtID);
    }

    public void updateTournament(String id, Tournament tournament)
    {
        tournamentDao.updateTournamentById(tournament);
    }

    public Player addPlayer(Player player) {
        Tournament tournament = tournamentDao.selectTournamentByCode(player.getRoomCode());

        if(!tournament.equals(null))
        {
            player.setTournamentID(tournament.getID());
            return playerDao.insertPlayer(player.getTournamentID(), player.getName(), player.getRoomCode(), player.getFormat(), player.getDeckName(), false);
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

    public List<MatchDataDTO> getMatchesByRoomCode(String code) {
        Tournament tournament = tournamentDao.selectTournamentByCode(code);
        List<MatchDataDTO> matchDataList = new ArrayList<>();

        if(tournament != null)
        {
            List<Match> matches = matchDao.selectMatchesInRound(tournament.getID(), tournament.getCurrRound());
            Player p1 = new Player();
            Player p2 = new Player();
            List<Game> gameList = new ArrayList<>();

            for(Match match : matches) {
                p1 = playerDao.selectPlayerById(match.getPlayer1ID());
                p2 = playerDao.selectPlayerById(match.getPlayer2ID());
                gameList = gameDao.selectGamesInMatch(match.getID());

                matchDataList.add(new MatchDataDTO(p1, p2, match, gameList));
            }
        }

        matchDataList.sort(Comparator.comparingInt(matchData -> matchData.match.getTableNum()));

        return matchDataList;
    }

    public MatchDataDTO getMatchByPlayerID(String playerID)
    {
        Player player = playerDao.selectPlayerById(playerID);
        Tournament tournament = tournamentDao.selectTournamentById(player.getTournamentID());
        Match match = matchDao.selectMatchByPlayerID(playerID, tournament.getCurrRound());
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

    public List<MatchDataDTO> generatePairings(String tournamentID)  {
        Tournament tournament = tournamentDao.selectTournamentById(tournamentID);


        if(!tournament.equals(null))
        {
            if (getMatchesByRoomCode(tournament.getRoomCode()).stream().allMatch(matchData -> matchData.match.getMatchStatus() == Match.MatchStatus.Complete.ordinal()))
            {
                if(tournament.getTournamentStatus() == Tournament.TournamentStatus.AwaitingStart.ordinal()) {
                    tournament.setTournamentStatus(Tournament.TournamentStatus.InProgress.ordinal());
                }

                tournament.incrementCurrRound();
                tournamentDao.updateTournamentById(tournament);

                List<Player> waitingPlayers = playerDao.selectPlayersByTournament(tournament.getRoomCode());

                Player bye = null;
                if (waitingPlayers.size() % 2 == 1)
                {
                    bye = playerDao.insertPlayer(tournament.getID(), "BYE", tournament.getRoomCode(), tournament.getFormat(), "", true);
                    waitingPlayers.add(bye);
                } else {
                    Optional<Player> byeMaybe = waitingPlayers.stream().filter(player -> player.getBye()).findFirst();
                    if (byeMaybe.isPresent()) {
                        bye = byeMaybe.get();
                    }
                }

                // Pair winning players with winning players and "randomize" players in each tier by sorting on the UUID assigned to the players ID
                // Secondary sort: randomly in asc or desc by ID
                Random rand = new Random();
                int randInt = rand.nextInt(100);
                boolean sortAsc = randInt % 2 == 1;
                if (sortAsc)
                {
                    waitingPlayers.sort(Comparator.comparing(Player::getID));
                }
                else
                {
                    waitingPlayers.sort(Comparator.comparing(Player::getID).reversed());
                }

                // Primary sort: players point totals
                waitingPlayers.sort(Comparator.comparing(Player::getPoints).reversed());

                int tableNum = 1;
                for (int i = 0; i < waitingPlayers.size(); i += 2) {
                    Match newMatch = matchDao.insertMatch(tournament.getID(), tournament.getNumGames(), waitingPlayers.get(i).getID(), waitingPlayers.get(i + 1).getID(), tableNum++, tournament.getCurrRound());

                    // Does this match have a bye?
                    if(bye != null && newMatch.playerIsInMatch(bye.getID())) {
                        // Report results for enough games to declare the real player winner
                        String realPlayerID = newMatch.getPlayer1ID().equals(bye.getID()) ? newMatch.getPlayer2ID() : newMatch.getPlayer1ID();
                        Player realPlayer = waitingPlayers.stream().filter(player -> player.getID().equals(realPlayerID)).findFirst().get();

                        for (double game = 0; game / (double) newMatch.getNumGames() < 0.5; game++) {
                            newMatch.addPlayerWin(realPlayerID);
                        }

                        realPlayer.addWinPoints(true);
                        playerDao.updatePlayerById(realPlayer);
                    }

                    matchDao.updateMatch(newMatch);
                }
            }

            return getMatchesByRoomCode(tournament.getRoomCode());
        }

        return new ArrayList<>();
    }

    public MatchDataDTO reportGameResults(String votingPlayerID, String winningPlayerID, int roundNum) {
        // Query for the corresponding match and game
        Match match = matchDao.selectMatchByPlayerID(votingPlayerID, roundNum);
        Game game = gameDao.selectGameById(match.getActiveGameID());
        Tournament tournament = tournamentDao.selectTournamentById(match.getTournamentID());

        // Report results
        int resultStatus = game.votePlayerWin(match, votingPlayerID, winningPlayerID);

        // Was this the final game?
        if(match.getMatchStatus() == Match.MatchStatus.InProgress.ordinal()) {
            if(resultStatus == Game.getResultStatusFinal())
            {
                // Results are in start a new game
                game.setIsActive(false);
                Game newGame = gameDao.insertGame(match.getID(), match.getTournamentID(), game.getGameNum() + 1);
                newGame.setIsActive(true);
                match.addNewActiveGameKey(newGame.getID());
            }
        }
        else {
            // The game is over tally the points
            if ("draw".equals(winningPlayerID)) {
                // They drew, each player gets a point
                Player p1 = playerDao.selectPlayerById(match.getPlayer1ID());
                Player p2 = playerDao.selectPlayerById(match.getPlayer2ID());

                p1.addDrawPoints();
                p2.addDrawPoints();

                playerDao.updatePlayerById(p1);
                playerDao.updatePlayerById(p2);
            } else {
                Player winner = playerDao.selectPlayerById(winningPlayerID);
                winner.addWinPoints(match.wasShutout(winningPlayerID));
                playerDao.updatePlayerById(winner);
            }

            game.setIsActive(false);
        }

        matchDao.updateMatch(match);
        gameDao.updateGame(game);

        // Was this the last match of the last round?
        if (tournament.getNumRounds() == match.getRoundNum() &&
                matchDao.selectMatchesInRound(tournament.getID(), tournament.getCurrRound())
                        .stream()
                        .allMatch(m -> m.getMatchStatus() == Match.MatchStatus.Complete.ordinal()))
        {
            tournament.setTournamentStatus(Tournament.TournamentStatus.Complete.ordinal());
            tournamentDao.updateTournamentById(tournament);
        }

        // Prepare MatchDatDTO
        List<Game> gameList = gameDao.selectGamesInMatch(match.getID());
        Player p1 = playerDao.selectPlayerById(match.getPlayer1ID());
        Player p2 = playerDao.selectPlayerById(match.getPlayer2ID());
        MatchDataDTO matchData = new MatchDataDTO(p1, p2, match, gameList);

        return matchData;
    }

    public MatchDataDTO setPlayerReady(String playerID, int roundNum) {
        Match match = matchDao.selectMatchByPlayerID(playerID, roundNum);
        match.playerReady(playerID);

        if(match.getMatchStatus() == Match.MatchStatus.InProgress.ordinal()) {
            Game newGame = gameDao.insertGame(match.getID(), match.getTournamentID(), 1);
            match.addNewActiveGameKey(newGame.getID());
            newGame.setIsActive(true);
            gameDao.updateGame(newGame);
        }

        matchDao.updateMatch(match);

        // Prepare MatchDatDTO
        List<Game> gameList = gameDao.selectGamesInMatch(match.getID());
        Player p1 = playerDao.selectPlayerById(match.getPlayer1ID());
        Player p2 = playerDao.selectPlayerById(match.getPlayer2ID());
        MatchDataDTO matchData = new MatchDataDTO(p1, p2, match, gameList);

        return matchData;
    }
}
