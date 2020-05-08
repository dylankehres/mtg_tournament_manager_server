package com.djk.tournament_manager.api;

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
    public UUID addTournament(@Valid @NonNull @RequestBody Tournament tournament)
    {
        return tournamentService.addTournament(tournament);
    }

    @PostMapping(path = "join")
    public UUID addPlayer(@NonNull @RequestBody Player player)
    {
        return tournamentService.addPlayer(player);
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping(path = "host/{id}")
    public Tournament getTournamentById(@PathVariable("id") UUID id)
    {
        return tournamentService.getTournamentById(id)
                .orElse(null);
    }

    @GetMapping(path = "host/pairings/{id}")
    public List<Match> getPairings(@PathVariable("id") UUID id)
    {
        return tournamentService.generatePairings(id);
    }

    @GetMapping(path = "join/{id}")
    public Player getPlayerById(@PathVariable("id") UUID id)
    {
        return tournamentService.getPlayerById(id)
                .orElse(null);
    }

    @GetMapping(path = "playerList/{id}")
    public List<Player> getPlayersInTournament(@PathVariable("id") String code){
         return tournamentService.getPlayersInTournament(code);
    }

    @GetMapping(path = "pairings/{code}")
    public List<Match> getPairings(@PathVariable("code") String code)
    {
        return tournamentService.getMatchesByRoomCode(code);
    }

    @GetMapping(path = "join/pairings/{id}")
    public Match getMatchForPlayer(@PathVariable("id") UUID id) {
        Optional<Match> matchMaybe = tournamentService.getMatchByPlayerID(id);
        if(matchMaybe.isPresent()) {
            return matchMaybe.get();
        }

        return new Match();
    }

    @DeleteMapping(path = "host")
    public void deleteTournamentById(@RequestBody UUID id)
    {
        tournamentService.deleteMatchByTournamentID(id);
        tournamentService.deleteTournament(id);
    }

    @DeleteMapping(path = "join")
    public void deletePlayerById(@RequestBody UUID id)
    {
        tournamentService.deletePlayer(id);
    }

    @PutMapping(path = "{id}")
    public void updateTournamentById(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody Tournament tournamentToUpdate)
    {
        tournamentService.updateTournament(id, tournamentToUpdate);
    }
}
