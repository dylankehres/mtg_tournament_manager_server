package com.djk.tournament_manager.api;

import com.djk.tournament_manager.dto.HostHubDTO;
import com.djk.tournament_manager.dto.MatchDataDTO;
import com.djk.tournament_manager.dto.PlayerHubDTO;
import com.djk.tournament_manager.dto.PlayerInfoDTO;
import com.djk.tournament_manager.model.Game;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.djk.tournament_manager.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@RequestMapping("api/v1/tournament")
@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.1.108:3000", "http://dylankehres.com"})
@RestController
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService)
    {
        this.tournamentService = tournamentService;
    }

    @PostMapping(path ="host")
    public String addTournament(@Valid @NonNull @RequestBody Tournament tournament)
    {
        return tournamentService.addTournament(tournament);
    }

    @PostMapping(path = "join")
    public String addPlayer(@NonNull @RequestBody Player player) {
        return tournamentService.addPlayer(player).getID();
    }

    @PostMapping(path = "/match/gameResults/{playerID}/{winnerID}/{roundNum}")
    public PlayerHubDTO receiveGameResults(@PathVariable("playerID") String votingPlayerID, @PathVariable("winnerID") String winningPlayerID, @PathVariable("roundNum") int roundNum) {
        return tournamentService.reportGameResults(votingPlayerID, winningPlayerID, roundNum);
    }

    @PostMapping(path =  "/match/ready/{playerID}/{roundNum}")
    public MatchDataDTO readyUp (@PathVariable("playerID") String playerID, @PathVariable("roundNum") int roundNum) {
        return tournamentService.setPlayerReady(playerID, roundNum);
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping(path = "tournament/{roomCode}")
    public Tournament getTournamentByCode(@PathVariable("roomCode") String roomCode) {
        return tournamentService.getTournamentByCode(roomCode);
    }

    @GetMapping(path = "host/pairings/{id}")
    public HostHubDTO generatePairings(@PathVariable("id") String id) {
        return tournamentService.generatePairings(id);
    }

    @GetMapping(path = "hostHub/{id}")
    public HostHubDTO getHostHubDTO(@PathVariable("id") String id) {
        return tournamentService.getHostHubDTO(id);
    }

    @GetMapping(path = "playerHub/{id}")
    public PlayerHubDTO getPlayerHubDTO(@PathVariable("id") String id) {
        return tournamentService.getPlayerHubDTO(id);
    }

    @GetMapping(path = "playerList/{id}")
    public List<Player> getPlayersInTournament(@PathVariable("id") String code){
         return tournamentService.getPlayersInTournament(code);
    }

    @GetMapping(path = "pairings/{code}")
    public List<MatchDataDTO> getPairings(@PathVariable("code") String code) {
        return tournamentService.getMatchesByRoomCode(code);
    }

    @GetMapping(path = "match/{id}")
    public MatchDataDTO getMatchForPlayer(@PathVariable("id") String id) {
        return tournamentService.getMatchByPlayerID(id);
    }

    @GetMapping(path = "playerInfo/{id}")
    public PlayerInfoDTO getPlayerInfo(@PathVariable("id") String id){
        return tournamentService.getPlayerInfoDTO(id);
    }

    @DeleteMapping(path = "host")
    public void deleteTournamentById(@RequestBody String id)
    {
        tournamentService.deleteTournamentData(id);
    }

    @DeleteMapping(path = "join")
    public void deletePlayerById(@RequestBody String id)
    {
        tournamentService.deletePlayer(id);
    }

    @PutMapping(path = "{id}")
    public void updateTournamentById(@PathVariable("id") String id, @Valid @NonNull @RequestBody Tournament tournamentToUpdate)
    {
        tournamentService.updateTournament(tournamentToUpdate);
    }
}
