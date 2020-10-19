package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.GameDao;
import com.djk.tournament_manager.dao.MatchDao;
import com.djk.tournament_manager.dao.PlayerDao;
import com.djk.tournament_manager.dao.TournamentDao;
import com.djk.tournament_manager.dto.HostHubDTO;
import com.djk.tournament_manager.dto.MatchDataDTO;
import com.djk.tournament_manager.dto.PlayerHubDTO;
import com.djk.tournament_manager.model.Game;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

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
        ArrayList<Player> players = playerDao.selectPlayersByTournamentID(tmtID);

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

    public ArrayList<Player> getPlayersInTournament(String code){
        return playerDao.selectPlayersByTournament(code);
    }

    public void deletePlayer(String id) { playerDao.deletePlayerById(id); }

    public ArrayList<MatchDataDTO> getMatchesByID(String id) {
        Tournament tournament = tournamentDao.selectTournamentById(id);
        ArrayList<MatchDataDTO> matchDataList = new ArrayList<>();

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

    public ArrayList<MatchDataDTO> getMatchesByRoomCode(String code) {
        Tournament tournament = tournamentDao.selectTournamentByCode(code);
        ArrayList<MatchDataDTO> matchDataList = new ArrayList<>();

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

    /**
     * Builds DTO for the PlayerHub component
     *
     * @param playerID ID of the player requesting the DTO
     * @return PlayerHubDTO
     */
    public PlayerHubDTO getPlayerHubDTO(String playerID) {
        Player currPlayer = playerDao.selectPlayerById(playerID);
        Tournament tournament = tournamentDao.selectTournamentById(currPlayer.getTournamentID());
        ArrayList<Player> playerList = getPlayersInTournament(currPlayer.getRoomCode());
        ArrayList<MatchDataDTO> pairings = getMatchesByID(tournament.getID());
        MatchDataDTO matchData = getMatchByPlayerID(currPlayer.getID());

        return new PlayerHubDTO(tournament, playerList, pairings, matchData, currPlayer);
    }

    /**
     * Builds DTO for the HostHub component
     *
     * @param tournamentID ID of the tournament requesting the DTO
     * @return HostHubDTO
     */
    public HostHubDTO getHostHubDTO(String tournamentID) {
        Tournament tournament = tournamentDao.selectTournamentById(tournamentID);
        ArrayList<MatchDataDTO> pairings = getMatchesByID(tournament.getID());
        ArrayList<Player> playerList = getPlayersInTournament(tournament.getRoomCode());

        return new HostHubDTO(tournament, pairings, playerList);
    }


    public int findUnmatchedOpponent(ArrayList<Player> waitingPlayers, String playerIDToMatch, int fromIndex, int toIndex)
    {

        for (int playerIndex = fromIndex; playerIndex < toIndex; playerIndex++) {
            if (matchDao.selectAllMatchesByPlayerID(waitingPlayers.get(playerIndex).getID())
                                .stream()
                                .noneMatch(match -> match.getPlayer1ID().equals(playerIDToMatch) || match.getPlayer2ID().equals(playerIDToMatch)))
            {
                return playerIndex;
            }
        }

        return -1;
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

                ArrayList<Player> waitingPlayers = playerDao.selectPlayersByTournament(tournament.getRoomCode());
                Optional<Player> byeMaybe = waitingPlayers.stream().filter(player -> player.getBye()).findFirst();
                Player bye = byeMaybe.isPresent() ? byeMaybe.get() : null;

                if (waitingPlayers.size() % 2 == 1)
                {
                    if(bye != null) { // A real player left the tournament, so we no longer need a bye
                        playerDao.deletePlayerById(bye.getID());
                        waitingPlayers.remove(bye);
                    } else {
                        bye = playerDao.insertPlayer(tournament.getID(), "BYE", tournament.getRoomCode(), tournament.getFormat(), "", true);
                        waitingPlayers.add(bye);
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

                boolean pairingSuccess;
                boolean acceptNonUniqueOpponents = false;
                int failedPairingsCount = 0;
                int tableNum = 1;
                ArrayList<Match> matches = new ArrayList<>();
                for (int i = 0; i < waitingPlayers.size(); i += 2) {
                    pairingSuccess = true;
                    String p1ID = waitingPlayers.get(i).getID();
                    String p2ID = waitingPlayers.get(i + 1).getID();
                    ArrayList<Match> p1PrevMatches = matchDao.selectAllMatchesByPlayerID(p1ID);

                    // Have these players played each other yet?
                    if(!acceptNonUniqueOpponents && p1PrevMatches.stream().anyMatch(match -> match.getPlayer1ID().equals(p2ID) || match.getPlayer2ID().equals(p2ID))) {
                        // Find the first unpairing player that p1 has not paired against yet
                        // We want to leave the top players with their best match opponent and rearrange the lower standings if possible
                        boolean swapPlayers = false;
                        int opponentIndex = findUnmatchedOpponent(waitingPlayers, p1ID, i, waitingPlayers.size());
                        Player opponent = new Player();

                        int playerIndex = i + 1;
                        Player player = waitingPlayers.get(playerIndex);

                        if (opponentIndex > -1) {
                            // These players have not faced each other, swap their opponents and repair the match
                            opponent = waitingPlayers.get(opponentIndex);
                            swapPlayers = true;
                            i-=2;

                            // Have we tried the every combination of pairings? Short circuit this logic and repair solely on standings
                            acceptNonUniqueOpponents = failedPairingsCount == waitingPlayers.size() * (waitingPlayers.size() - 1);

                        } else {
                            // All unpaired players faced this player find a paired player
                            // Find the first unpairing player that p1 has not paired against yet
                            opponentIndex = findUnmatchedOpponent(waitingPlayers, p1ID, 0, i);
                            if (opponentIndex > -1) {
                                // These players have not faced each other, swap their opponents
                                opponent = waitingPlayers.get(opponentIndex);
                                swapPlayers = true;

                                // We need to repair all players, clear everything and restart
                                i = 0;
                                tableNum = 0;
                                matches = new ArrayList<>();
                                failedPairingsCount++;

                                // Have we tried the every combination of pairings? Short circuit this logic and repair solely on standings
                                acceptNonUniqueOpponents = failedPairingsCount == waitingPlayers.size() * (waitingPlayers.size() - 1);
                            }
                        }

                        if (swapPlayers) {
                            waitingPlayers.set(opponentIndex, player);
                            waitingPlayers.set(playerIndex, opponent);

                            pairingSuccess = false;
                        }
                    }

                    if (pairingSuccess){
                        Match newMatch = new Match("", tournament.getID(), tournament.getNumGames(), p1ID, p2ID, tableNum++, tournament.getCurrRound());
                        matches.add(newMatch);
                    }
                }

                Match savedMatch;
                for(Match newMatch : matches) {
                    savedMatch = matchDao.insertMatch(newMatch);

                    // Does this match have a bye?
                    if (bye != null && savedMatch.playerIsInMatch(bye.getID())) {
                        // Report results for enough games to declare the real player winner
                        String realPlayerID = savedMatch.getPlayer1ID().equals(bye.getID()) ? savedMatch.getPlayer2ID() : savedMatch.getPlayer1ID();
                        Player realPlayer = waitingPlayers.stream().filter(player -> player.getID().equals(realPlayerID)).findFirst().get();

                        for (double game = 0; game / (double) savedMatch.getNumGames() < 0.5; game++) {
                            savedMatch.addPlayerWin(realPlayerID);
                        }

                        realPlayer.addWinPoints(true);
                        playerDao.updatePlayerById(realPlayer);
                        matchDao.updateMatch(savedMatch);
                    }
                }
            }

            return getMatchesByRoomCode(tournament.getRoomCode());
        }

        return new ArrayList<>();
    }

    public PlayerHubDTO reportGameResults(String votingPlayerID, String winningPlayerID, int roundNum) {
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

        // Prepare PlayerHubDTO
//        List<Game> gameList = gameDao.selectGamesInMatch(match.getID());
//        Player p1 = playerDao.selectPlayerById(match.getPlayer1ID());
//        Player p2 = playerDao.selectPlayerById(match.getPlayer2ID());
//        MatchDataDTO matchData = new MatchDataDTO(p1, p2, match, gameList);
//        return matchData;

        return getPlayerHubDTO(votingPlayerID);
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
