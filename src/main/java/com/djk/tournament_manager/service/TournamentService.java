package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.game.GameDAO;
import com.djk.tournament_manager.dao.match.MatchDAO;
import com.djk.tournament_manager.dao.player.PlayerDAO;
import com.djk.tournament_manager.dao.tournament.TournamentDAO;
import com.djk.tournament_manager.dto.HostHubDTO;
import com.djk.tournament_manager.dto.MatchDataDTO;
import com.djk.tournament_manager.dto.PlayerHubDTO;
import com.djk.tournament_manager.dto.PlayerInfoDTO;
import com.djk.tournament_manager.model.Game;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.djk.tournament_manager.tools.StableRoommates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@Service
public class TournamentService {

    private final TournamentDAO tournamentDAO;
    private final PlayerDAO playerDAO;
    private final MatchDAO matchDAO;
    private final GameDAO gameDAO;

    @Autowired
    public TournamentService(@Qualifier("firebaseTournamentDao") TournamentDAO tournamentDAO, @Qualifier("firebasePlayerDao") PlayerDAO playerDAO, @Qualifier("firebaseMatchDao") MatchDAO matchDAO, @Qualifier("firebaseGameDao") GameDAO gameDAO)
    {
        this.tournamentDAO = tournamentDAO;
        this.playerDAO = playerDAO;
        this.matchDAO = matchDAO;
        this.gameDAO = gameDAO;
    }

    public String addTournament(Tournament tournament)
    {
        if(tournamentDAO.selectTournamentByCode(tournament.getRoomCode()) != null) {
            return "-1";
        }

        Tournament newTournament = tournamentDAO.insert(tournament);

        if (newTournament != null) {
            return newTournament.getID();
        }

        return "-1";
    }

    public List<Tournament> getAllTournaments()
    {
         return tournamentDAO.selectAll();
    }

    public List<Tournament> getTournamentsByStatus(int status)
    {
        return tournamentDAO.selectTournamentByStatus(status);
    }

    public Tournament getTournamentByCode(String roomCode) {
        return tournamentDAO.selectTournamentByCode(roomCode);
    }

    public Tournament getTournamentByID(String id) {
        return tournamentDAO.selectById(id);
    }

    public void deleteTournamentData(String tmtID) {
        // Query for all objects that are children of this tournament
        List<Match> matches = matchDAO.selectMatchesInTournament(tmtID);
        List<Game> games = gameDAO.selectGamesInTournament(tmtID);
        ArrayList<Player> players = playerDAO.selectPlayersByTournamentID(tmtID);

        // Delete all tournament data
        for(Player player : players) {
            playerDAO.deleteById(player.getID());
        }

        for(Game game : games) {
            gameDAO.deleteById(game.getID());
        }

        for(Match match : matches) {
            matchDAO.deleteById(match.getID());
        }

        tournamentDAO.deleteById(tmtID);
    }

    public void updateTournament(Tournament tournament)
    {
        tournamentDAO.update(tournament);
    }

    public Player addPlayer(Player player) {
        Tournament tournament = tournamentDAO.selectTournamentByCode(player.getRoomCode());

        if(tournament.getID() != null)
        {
            player.setTournamentID(tournament.getID());
            return playerDAO.insert(player);
        }

        return null;
    }

    public ArrayList<Player> getPlayersInTournament(String code){
        return playerDAO.selectPlayersByTournamentCode(code);
    }

    public void deletePlayer(String id) { playerDAO.deleteById(id); }

    public ArrayList<MatchDataDTO> getMatchesByTournamentID(String id) {
        Tournament tournament = tournamentDAO.selectById(id);
        ArrayList<MatchDataDTO> matchDataList = new ArrayList<>();

        if(tournament != null)
        {
            List<Match> matches = matchDAO.selectMatchesInRound(tournament.getID(), tournament.getCurrRound());

            for(Match match : matches) {
                Player p1 = playerDAO.selectById(match.getPlayer1ID());
                Player p2 = playerDAO.selectById(match.getPlayer2ID());
                List<Game>  gameList = gameDAO.selectGamesInMatch(match.getID());

                matchDataList.add(new MatchDataDTO(p1, p2, match, gameList));
            }
        }

        matchDataList.sort(Comparator.comparingInt(matchData -> matchData.match.getTableNum()));

        return matchDataList;
    }

    public ArrayList<MatchDataDTO> getMatchesByRoomCode(String code) {
        Tournament tournament = tournamentDAO.selectTournamentByCode(code);
        ArrayList<MatchDataDTO> matchDataList = new ArrayList<>();

        if(tournament != null)
        {
            List<Match> matches = matchDAO.selectMatchesInRound(tournament.getID(), tournament.getCurrRound());

            for(Match match : matches) {
                Player p1 = playerDAO.selectById(match.getPlayer1ID());
                Player p2 = playerDAO.selectById(match.getPlayer2ID());
                List<Game> gameList = gameDAO.selectGamesInMatch(match.getID());

                matchDataList.add(new MatchDataDTO(p1, p2, match, gameList));
            }
        }

        matchDataList.sort(Comparator.comparingInt(matchData -> matchData.match.getTableNum()));

        return matchDataList;
    }

    public MatchDataDTO getMatchByPlayerID(String playerID)
    {
        return getMatchByPlayerID(playerID, -1);
    }

    public MatchDataDTO getMatchByPlayerID(String playerID, int roundNum)
    {
        Player player = playerDAO.selectById(playerID);
        Tournament tournament = tournamentDAO.selectById(player.getTournamentID());

        if(roundNum == -1) {
            roundNum = tournament.getCurrRound();
        }

        Match match = matchDAO.selectMatchByPlayerID(playerID, roundNum);
        Player p1 = new Player();
        Player p2 = new Player();
        List<Game> gameList = new ArrayList<>();

        if(match != null) {
            if(match.getPlayer1ID().equals(playerID)) {
                p1 = player;
                p2 = playerDAO.selectById(match.getPlayer2ID());
            } else {
                p1 = playerDAO.selectById(match.getPlayer1ID());
                p2 = player;
            }
            gameList = gameDAO.selectGamesInMatch(match.getID());
        }
        else {
            match = new Match();
        }

        return new MatchDataDTO(p1, p2, match, gameList);
    }

    public ArrayList<MatchDataDTO> getAllMatchesByPlayerID(String playerID)
    {
        ArrayList<MatchDataDTO> matchDataList = new ArrayList<>();
        Player player = playerDAO.selectById(playerID);
        ArrayList<Match> matches = matchDAO.selectAllMatchesByPlayerID(playerID);

        for(Match match : matches) {
            Player p1;
            Player p2;

            if (match.getPlayer1ID().equals(playerID)) {
                p1 = player;
                p2 = playerDAO.selectById(match.getPlayer2ID());
            } else {
                p1 = playerDAO.selectById(match.getPlayer1ID());
                p2 = player;
            }
            
            List<Game> gameList = gameDAO.selectGamesInMatch(match.getID());

            matchDataList.add(new MatchDataDTO(p1, p2, match, gameList));
        }

        return matchDataList;
    }

    public void deleteMatchByTournamentID(String tournamentID)
    {
        List<Match> matchesToDelete = matchDAO.selectMatchesInTournament(tournamentID);

        for (Match m : matchesToDelete) {
            matchDAO.deleteById(m.getID());
        }
    }

    /**
     * Builds DTO for the PlayerHub component
     *
     * @param playerID ID of the player requesting the DTO
     * @return PlayerHubDTO
     */
    public PlayerHubDTO getPlayerHubDTO(String playerID) {
        Player currPlayer = playerDAO.selectById(playerID);
        Tournament tournament = tournamentDAO.selectById(currPlayer.getTournamentID());
        ArrayList<Player> playerList = getPlayersInTournament(currPlayer.getRoomCode());
        ArrayList<MatchDataDTO> pairings = getMatchesByTournamentID(tournament.getID());
        MatchDataDTO matchData = getMatchByPlayerID(currPlayer.getID());

        return new PlayerHubDTO(tournament, playerList, pairings, matchData, currPlayer);
    }

    /**
     * Builds DTO for the PlayerInfo component
     *
     * @param playerID ID of the player requesting the DTO
     * @return PlayerInfoDTO
     */
    public PlayerInfoDTO getPlayerInfoDTO(String playerID) {
        Player player = playerDAO.selectById(playerID);
        Tournament tournament = tournamentDAO.selectById(player.getTournamentID());
        ArrayList<MatchDataDTO> matches = getAllMatchesByPlayerID(playerID);

        return new PlayerInfoDTO(tournament, player, matches);
    }

    /**
     * Builds DTO for the HostHub component
     *
     * @param tournamentID ID of the tournament requesting the DTO
     * @return HostHubDTO
     */
    public HostHubDTO getHostHubDTO(String tournamentID) {
        Tournament tournament = tournamentDAO.selectById(tournamentID);
        ArrayList<MatchDataDTO> pairings = getMatchesByTournamentID(tournament.getID());
        ArrayList<Player> playerList = getPlayersInTournament(tournament.getRoomCode());

        return new HostHubDTO(tournament, pairings, playerList);
    }


//    public int findUnmatchedOpponent(ArrayList<Player> waitingPlayers, String playerIDToMatch, int fromIndex, int toIndex)
//    {
//
//        for (int playerIndex = fromIndex; playerIndex < toIndex; playerIndex++) {
//            if (matchDAO.selectAllMatchesByPlayerID(waitingPlayers.get(playerIndex).getID())
//                                .stream()
//                                .noneMatch(match -> match.getPlayer1ID().equals(playerIDToMatch) || match.getPlayer2ID().equals(playerIDToMatch)))
//            {
//                return playerIndex;
//            }
//        }
//
//        return -1;
//    }

    public HostHubDTO generatePairings(String tournamentID)  {
        Tournament tournament = tournamentDAO.selectById(tournamentID);

        if(tournament != null)
        {
            if (getMatchesByRoomCode(tournament.getRoomCode()).stream().allMatch(matchData -> matchData.match.getMatchStatus() == Match.MatchStatus.Complete.ordinal())) {
                if (tournament.getTournamentStatus() == Tournament.TournamentStatus.AwaitingStart.ordinal()) {
                    tournament.setTournamentStatus(Tournament.TournamentStatus.InProgress.ordinal());
                }

                tournament.incrementCurrRound();

                ArrayList<Player> waitingPlayers = playerDAO.selectPlayersByTournamentCode(tournament.getRoomCode());
                Optional<Player> byeMaybe = waitingPlayers.stream().filter(Player::getBye).findFirst();
                Player bye = byeMaybe.orElse(null);

                if (waitingPlayers.size() % 2 == 1) {
                    if (bye != null) { // A real player left the tournament, so we no longer need a bye
                        playerDAO.deleteById(bye.getID());
                        waitingPlayers.remove(bye);
                    } else {
                        bye = playerDAO.insert(new Player(tournament.getID(), "BYE", tournament.getRoomCode(), "", "", true));
                        waitingPlayers.add(bye);
                    }
                }

                StableRoommates srp = new StableRoommates(matchDAO, tournament, waitingPlayers);
                ArrayList<Match> matches = srp.getPairings();

                if (waitingPlayers.size() / 2 == matches.size()) {
                    tournamentDAO.update(tournament);

                    Match savedMatch;
                    for (Match newMatch : matches) {
//                        savedMatch = matchDAO.insert(newMatch);

                        if (newMatch != null) {
                            // Does this match have a bye?
                            if (bye != null && newMatch.playerIsInMatch(bye.getID())) {
                                // Report results for enough games to declare the real player winner
                                String realPlayerID = newMatch.getPlayer1ID().equals(bye.getID()) ? newMatch.getPlayer2ID() : newMatch.getPlayer1ID();
                                Optional<Player> playerMaybe = waitingPlayers.stream().filter(player -> player.getID().equals(realPlayerID)).findFirst();
                                Player realPlayer = playerMaybe.orElse(null);

                                for (double game = 0; game / (double) newMatch.getNumGames() < 0.5; game++) {
                                    newMatch.addPlayerWin(realPlayerID);
                                }

                                if (realPlayer != null) {
                                    realPlayer.addWinPoints(true);
                                    playerDAO.update(realPlayer);
//                                    matchDAO.update(savedMatch);
                                }
                            }
                        }

                        matchDAO.insert(newMatch);
                    }
                }
            }

            return getHostHubDTO(tournamentID);
        }

        return new HostHubDTO();
    }

    public PlayerHubDTO reportGameResults(String votingPlayerID, String winningPlayerID, int roundNum) {
        // Query for the corresponding match and game
        Match match = matchDAO.selectMatchByPlayerID(votingPlayerID, roundNum);
        Game game = gameDAO.selectById(match.getActiveGameID());
        Tournament tournament = tournamentDAO.selectById(match.getTournamentID());

        // Report results
        int resultStatus = game.votePlayerWin(match, votingPlayerID, winningPlayerID);

        // Was this the final game?
        if(match.getMatchStatus() == Match.MatchStatus.InProgress.ordinal()) {
            if(resultStatus == Game.getResultStatusFinal())
            {
                // Results are in start a new game
                game.setActive(false);
                Game newGame = gameDAO.insert(new Game(match.getID(), match.getTournamentID(), game.getGameNum() + 1));

                if(newGame != null) {
                    newGame.setActive(true);
                    match.addNewActiveGameKey(newGame.getID());
                }
            }
        }
        else {
            // The game is over tally the points
            if ("draw".equals(winningPlayerID)) {
                // They drew, each player gets a point
                Player p1 = playerDAO.selectById(match.getPlayer1ID());
                Player p2 = playerDAO.selectById(match.getPlayer2ID());

                p1.addDrawPoints();
                p2.addDrawPoints();

                playerDAO.update(p1);
                playerDAO.update(p2);
            } else {
                Player winner = playerDAO.selectById(winningPlayerID);
                winner.addWinPoints(match.wasShutout(winningPlayerID));
                playerDAO.update(winner);
            }

            game.setActive(false);
        }

        matchDAO.update(match);
        gameDAO.update(game);

        // Was this the last match of the last round?
        if (tournament.getNumRounds() == match.getRoundNum() &&
                matchDAO.selectMatchesInRound(tournament.getID(), tournament.getCurrRound())
                        .stream()
                        .allMatch(m -> m.getMatchStatus() == Match.MatchStatus.Complete.ordinal()))
        {
            tournament.setTournamentStatus(Tournament.TournamentStatus.Complete.ordinal());
            tournamentDAO.update(tournament);
        }

        return getPlayerHubDTO(votingPlayerID);
    }

    public MatchDataDTO setPlayerReady(String playerID, int roundNum) {
        Match match = matchDAO.selectMatchByPlayerID(playerID, roundNum);
        match.playerReady(playerID);

        if(match.getMatchStatus() == Match.MatchStatus.InProgress.ordinal()) {
            Game newGame = gameDAO.insert(new Game(match.getID(), match.getTournamentID(), 1));

            if (newGame != null) {
                match.addNewActiveGameKey(newGame.getID());
                newGame.setActive(true);
                gameDAO.update(newGame);
            }
        }

        matchDAO.update(match);

        // Prepare MatchDatDTO
        List<Game> gameList = gameDAO.selectGamesInMatch(match.getID());
        Player p1 = playerDAO.selectById(match.getPlayer1ID());
        Player p2 = playerDAO.selectById(match.getPlayer2ID());

        return new MatchDataDTO(p1, p2, match, gameList);
    }
}
