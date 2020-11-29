package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.game.GameDAS;
import com.djk.tournament_manager.dao.match.MatchDAS;
import com.djk.tournament_manager.dao.player.PlayerDAS;
import com.djk.tournament_manager.dao.tournament.TournamentDAS;
import com.djk.tournament_manager.dto.HostHubDTO;
import com.djk.tournament_manager.dto.MatchDataDTO;
import com.djk.tournament_manager.dto.PlayerHubDTO;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TournamentServiceTest {
    FirebaseInit firebaseInit;
    TournamentService tournamentService;
    String tournamentID;
    ArrayList<Player> players;

    @BeforeEach
    void setUp() {
        firebaseInit = new FirebaseInit();
        try {
            firebaseInit.init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tournamentService = new TournamentService(new TournamentDAS(), new PlayerDAS(), new MatchDAS(), new GameDAS());
        tournamentID = tournamentService.addTournament(new Tournament("TestTournament", "test", "Modern", 5, 3));

        players = new ArrayList<>();
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Dylan", "test", "Modern", "Devoted Druid", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Keir", "test", "Modern", "Shadow", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Bobby", "test", "Modern", "Scales", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Andy", "test", "Modern", "Bant Soulherder", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Aaron", "test", "Modern", "UR Kiki", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "BYE", "test", "Modern", "", true)));
    }

    @AfterEach
    void tearDown() {
        tournamentService.deleteTournamentData(tournamentID);
    }

    void readyUpPlayers(int roundNum) {
        for(Player player : players) {
            tournamentService.setPlayerReady(player.getID(), roundNum);
        }
    }

    void reportGameResultsTest(String winningPlayerID, String losingPlayerID, int gameIndex, int roundNum) {
        PlayerHubDTO winnerDTO = tournamentService.reportGameResults(winningPlayerID, winningPlayerID, roundNum);
        assertEquals("-1", winnerDTO.matchData.gameList.get(gameIndex).getWinningPlayerID());

        PlayerHubDTO loserDTO = tournamentService.reportGameResults(losingPlayerID, winningPlayerID, roundNum);
        winnerDTO = tournamentService.getPlayerHubDTO(winningPlayerID);

        assertEquals(winningPlayerID, loserDTO.matchData.gameList.get(gameIndex).getWinningPlayerID());
        assertEquals(winningPlayerID, winnerDTO.matchData.gameList.get(gameIndex).getWinningPlayerID());
    }

    void checkByeResults() {
        String byeID = players.stream().filter(Player::getBye).findFirst().orElse(new Player()).getID();

        if(!byeID.isEmpty()) {
            PlayerHubDTO byeDTO = tournamentService.getPlayerHubDTO(byeID);

            if (byeDTO.matchData.match.getPlayer1ID().equals(byeID)) {
                assertEquals(2, byeDTO.matchData.match.getPlayer2Wins());
            } else {
                assertEquals(2, byeDTO.matchData.match.getPlayer1Wins());
            }
        }
    }

    void generateRoundOnePairings() {
        HostHubDTO hostHubDTO = tournamentService.generatePairings(tournamentID);
        ArrayList<MatchDataDTO> matches = hostHubDTO.pairings;

        assertEquals(3, matches.size());
    }

    void generateRoundTwoPairings() {
        int roundNum = 1;
        readyUpPlayers(roundNum);
        ArrayList<MatchDataDTO> roundOneMatches = tournamentService.getMatchesByTournamentID(tournamentID);
        Player bye = players.stream().filter(Player::getBye).findFirst().orElse(new Player());

        for (int i = 0; i < roundOneMatches.size(); i++) {
            if(!roundOneMatches.get(i).match.playerIsInMatch(bye.getID())) {
                switch (i) {
                    case 0:
                        reportGameResultsTest(roundOneMatches.get(i).player1.getID(), roundOneMatches.get(i).player2.getID(), 0, roundNum);
                        reportGameResultsTest(roundOneMatches.get(i).player1.getID(), roundOneMatches.get(i).player2.getID(), 1, roundNum);
                        break;
                    case 1:
                    case 2:
                        reportGameResultsTest(roundOneMatches.get(i).player1.getID(), roundOneMatches.get(i).player2.getID(), 0, roundNum);
                        reportGameResultsTest(roundOneMatches.get(i).player2.getID(), roundOneMatches.get(i).player1.getID(), 1, roundNum);
                        reportGameResultsTest(roundOneMatches.get(i).player1.getID(), roundOneMatches.get(i).player2.getID(), 2, roundNum);
                        break;
                }
            }
        }

        checkByeResults();

        HostHubDTO hostHubDTO = tournamentService.generatePairings(tournamentID);
        ArrayList<MatchDataDTO> roundTwoMatches = hostHubDTO.pairings;
        assertEquals(3, roundTwoMatches.size());
    }

    void generateRoundThreePairings() {
    }

    @Test
    void testThreeRoundTournament() {
        generateRoundOnePairings();
        generateRoundTwoPairings();
    }
}