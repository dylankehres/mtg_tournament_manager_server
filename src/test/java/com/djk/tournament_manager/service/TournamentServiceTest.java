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
        tournamentID = tournamentService.addTournament(new Tournament("TestTournament", "test", "Modern", 3, 3));

        players = new ArrayList<>();
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Dylan", "test", "Modern", "Devoted Druid", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Keir", "test", "Modern", "Shadow", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Bobby", "test", "Modern", "Scales", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Andy", "test", "Modern", "Bant Soulherder", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Aaron", "test", "Modern", "UR Kiki", false)));
        players.add(tournamentService.addPlayer(new Player(tournamentID, "Joe", "test", "Modern", "Humans", false)));
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

    void reportRoundResults(String winningPlayerID, String losingPlayerID, int roundNum, boolean shutout) {
        int gameIndex = 0;
        reportGameResultsTest(winningPlayerID, losingPlayerID, gameIndex++, roundNum);

        if(!shutout) {
            reportGameResultsTest(losingPlayerID, winningPlayerID, gameIndex++, roundNum);
        }

        reportGameResultsTest(winningPlayerID, losingPlayerID, gameIndex, roundNum);
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
        ArrayList<Player> shutoutWinners = new ArrayList<>();
        ArrayList<Player> nonShutoutWinners = new ArrayList<>();
        ArrayList<Player> losers = new ArrayList<>();

        for (int i = 0; i < roundOneMatches.size(); i++) {
            if(roundOneMatches.get(i).match.playerIsInMatch(bye.getID())) {
                if(roundOneMatches.get(i).player1.getID().equals(bye.getID())) {
                    shutoutWinners.add(roundOneMatches.get(i).player2);
                    losers.add(roundOneMatches.get(i).player1);
                } else {
                    shutoutWinners.add(roundOneMatches.get(i).player1);
                    losers.add(roundOneMatches.get(i).player2);
                }

            } else {
                switch (i) {
                    case 0:
                    case 1:
                        reportRoundResults(roundOneMatches.get(i).player1.getID(), roundOneMatches.get(i).player2.getID(), roundNum, true);
                        shutoutWinners.add(roundOneMatches.get(i).player1);
                        losers.add(roundOneMatches.get(i).player2);
                        break;

                    case 2:
                        reportRoundResults(roundOneMatches.get(i).player1.getID(), roundOneMatches.get(i).player2.getID(), roundNum,false);
                        nonShutoutWinners.add(roundOneMatches.get(i).player1);
                        losers.add(roundOneMatches.get(i).player2);
                        break;
                }
            }
        }

        checkByeResults();

        HostHubDTO hostHubDTO = tournamentService.generatePairings(tournamentID);
        ArrayList<MatchDataDTO> roundTwoMatches = hostHubDTO.pairings;
        assertEquals(3, roundTwoMatches.size());

        MatchDataDTO round2ForRound1Table1Winner = roundTwoMatches.stream()
                .filter(matchDataDTO -> matchDataDTO.match.playerIsInMatch(shutoutWinners.get(0).getID()))
                .findFirst()
                .orElse(new MatchDataDTO());

        if (!round2ForRound1Table1Winner.match.playerIsInMatch(shutoutWinners.get(1).getID())) {
            throw new AssertionError();
        }

        MatchDataDTO round2ForRound1Table3Winner = roundTwoMatches.stream()
                .filter(matchDataDTO -> matchDataDTO.match.playerIsInMatch(nonShutoutWinners.get(0).getID()))
                .findFirst()
                .orElse(new MatchDataDTO());

        if (!round2ForRound1Table3Winner.match.playerIsInMatch(losers.get(0).getID()) && !round2ForRound1Table3Winner.match.playerIsInMatch(losers.get(1).getID())) {
            throw new AssertionError();
        }
    }

    void generateRoundThreePairings() {
        int roundNum = 2;
        readyUpPlayers(roundNum);
        ArrayList<MatchDataDTO> roundTwoMatches = tournamentService.getMatchesByTournamentID(tournamentID);
        Player bye = players.stream().filter(Player::getBye).findFirst().orElse(new Player());
        ArrayList<Player> shutoutWinners = new ArrayList<>();
        ArrayList<Player> nonShutoutWinners = new ArrayList<>();
        ArrayList<Player> losers = new ArrayList<>();

        for (int i = 0; i < roundTwoMatches.size(); i++) {
            if(roundTwoMatches.get(i).match.playerIsInMatch(bye.getID())) {
                if(roundTwoMatches.get(i).player1.getID().equals(bye.getID())) {
                    shutoutWinners.add(roundTwoMatches.get(i).player2);
                    losers.add(roundTwoMatches.get(i).player1);
                } else {
                    shutoutWinners.add(roundTwoMatches.get(i).player1);
                    losers.add(roundTwoMatches.get(i).player2);
                }

            } else {
                switch (i) {
                    case 0:
                    case 1:
                        reportRoundResults(roundTwoMatches.get(i).player1.getID(), roundTwoMatches.get(i).player2.getID(), roundNum, true);
                        shutoutWinners.add(roundTwoMatches.get(i).player1);
                        losers.add(roundTwoMatches.get(i).player2);
                        break;

                    case 2:
                        reportRoundResults(roundTwoMatches.get(i).player1.getID(), roundTwoMatches.get(i).player2.getID(), roundNum,false);
                        nonShutoutWinners.add(roundTwoMatches.get(i).player1);
                        losers.add(roundTwoMatches.get(i).player2);
                        break;
                }
            }
        }

        checkByeResults();

        HostHubDTO hostHubDTO = tournamentService.generatePairings(tournamentID);
        ArrayList<MatchDataDTO> roundThreeMatches = hostHubDTO.pairings;
        assertEquals(3, roundThreeMatches.size());

        MatchDataDTO round3ForRound2Table1Winner = roundThreeMatches.stream()
                .filter(matchDataDTO -> matchDataDTO.match.playerIsInMatch(shutoutWinners.get(0).getID()))
                .findFirst()
                .orElse(new MatchDataDTO());

        if (!round3ForRound2Table1Winner.match.playerIsInMatch(shutoutWinners.get(1).getID())) {
            throw new AssertionError();
        }

        MatchDataDTO round3ForRound2Table3Winner = roundThreeMatches.stream()
                .filter(matchDataDTO -> matchDataDTO.match.playerIsInMatch(nonShutoutWinners.get(0).getID()))
                .findFirst()
                .orElse(new MatchDataDTO());

        Player roundTwoLoserWith3Points = losers.stream()
                .filter(player -> player.getPoints() == 3)
                .findFirst()
                .orElse(new Player());

        if (!round3ForRound2Table3Winner.match.playerIsInMatch(roundTwoLoserWith3Points.getID())) {
            throw new AssertionError();
        }
    }

    void resultRoundThree() {
        int roundNum = 3;
        readyUpPlayers(roundNum);
        ArrayList<MatchDataDTO> roundThreeMatches = tournamentService.getMatchesByTournamentID(tournamentID);
        Player bye = players.stream().filter(Player::getBye).findFirst().orElse(new Player());
        ArrayList<Player> shutoutWinners = new ArrayList<>();
        ArrayList<Player> nonShutoutWinners = new ArrayList<>();
        ArrayList<Player> losers = new ArrayList<>();

        for (int i = 0; i < roundThreeMatches.size(); i++) {
            if (roundThreeMatches.get(i).match.playerIsInMatch(bye.getID())) {
                if (roundThreeMatches.get(i).player1.getID().equals(bye.getID())) {
                    shutoutWinners.add(roundThreeMatches.get(i).player2);
                    losers.add(roundThreeMatches.get(i).player1);
                } else {
                    shutoutWinners.add(roundThreeMatches.get(i).player1);
                    losers.add(roundThreeMatches.get(i).player2);
                }

            } else {
                switch (i) {
                    case 0:
                    case 1:
                        reportRoundResults(roundThreeMatches.get(i).player1.getID(), roundThreeMatches.get(i).player2.getID(), roundNum, true);
                        shutoutWinners.add(roundThreeMatches.get(i).player1);
                        losers.add(roundThreeMatches.get(i).player2);
                        break;

                    case 2:
                        reportRoundResults(roundThreeMatches.get(i).player1.getID(), roundThreeMatches.get(i).player2.getID(), roundNum, false);
                        nonShutoutWinners.add(roundThreeMatches.get(i).player1);
                        losers.add(roundThreeMatches.get(i).player2);
                        break;
                }
            }
        }

        checkByeResults();

        Tournament tournament = tournamentService.getAllTournaments().stream()
                .filter(tmt -> tmt.getID().equals(tournamentID))
                .findFirst()
                .orElse(new Tournament());

        if(tournament.getTournamentStatus() != Tournament.TournamentStatus.Complete.ordinal()) {
            throw new AssertionError();
        }

        players = tournamentService.getPlayersInTournament(tournament.getRoomCode());

        if(players.get(0).getPoints() != 9) {
            throw new AssertionError();
        }

        if(players.get(1).getPoints() != 6) {
            throw new AssertionError();
        }

        if(players.get(2).getPoints() != 5) {
            throw new AssertionError();
        }

        if(players.get(3).getPoints() != 2) {
            throw new AssertionError();
        }

        if(players.get(4).getPoints() != 2) {
            throw new AssertionError();
        }

        if(players.get(5).getPoints() != 0) {
            throw new AssertionError();
        }
    }

    @Test
    void testThreeRoundTournament() {
        generateRoundOnePairings();
        generateRoundTwoPairings();
        generateRoundThreePairings();
        resultRoundThree();
    }
}