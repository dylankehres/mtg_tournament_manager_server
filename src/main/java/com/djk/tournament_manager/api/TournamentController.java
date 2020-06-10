package com.djk.tournament_manager.api;

import com.djk.tournament_manager.dto.MatchDataDTO;
import com.djk.tournament_manager.model.Match;
import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.djk.tournament_manager.service.TournamentService;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte1.other;


@RequestMapping("api/v1/tournament")
//@CrossOrigin(origins = "http://localhost:3000/")
@CrossOrigin
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
        return tournamentService.addPlayer(player);
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping(path = "host/{id}")
    public Tournament getTournamentById(@PathVariable("id") String id)
    {
        Tournament tournament = tournamentService.getTournamentById(id);

        if(!tournament.equals(null)){
            return tournament;
        }
        else {
            return null;
        }
    }

    @GetMapping(path = "host/pairings/{id}")
    public List<MatchDataDTO> generatePairings(@PathVariable("id") String id) {
        return tournamentService.generatePairings(id);
    }

    @GetMapping(path = "join/{id}")
    public Player getPlayerById(@PathVariable("id") String id) {
        return tournamentService.getPlayerById(id);
    }

    @GetMapping(path = "playerList/{id}")
    public List<Player> getPlayersInTournament(@PathVariable("id") String code){
         return tournamentService.getPlayersInTournament(code);
    }

    @GetMapping(path = "pairings/{code}")
    public List<MatchDataDTO> getPairings(@PathVariable("code") String code) {
        return tournamentService.getMatchesByRoomCode(code);
    }

    @GetMapping(path = "join/pairings/{id}")
    public MatchDataDTO getMatchForPlayer(@PathVariable("id") String id) {
        return tournamentService.getMatchByPlayerID(id);
    }

    @DeleteMapping(path = "host")
    public void deleteTournamentById(@RequestBody String id)
    {
        tournamentService.deleteMatchByTournamentID(id);
        tournamentService.deleteTournament(id);
    }

    @DeleteMapping(path = "join")
    public void deletePlayerById(@RequestBody String id)
    {
        tournamentService.deletePlayer(id);
    }

    @PutMapping(path = "{id}")
    public void updateTournamentById(@PathVariable("id") String id, @Valid @NonNull @RequestBody Tournament tournamentToUpdate)
    {
        tournamentService.updateTournament(id, tournamentToUpdate);
    }
}
