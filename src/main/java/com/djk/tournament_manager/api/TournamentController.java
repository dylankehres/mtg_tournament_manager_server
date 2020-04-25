package com.djk.tournament_manager.api;

import com.djk.tournament_manager.model.Tournament;
import com.djk.tournament_manager.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte1.other;

@RequestMapping("api/v1/tournament")
@RestController
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService)
    {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public void addTournament(@Valid @NonNull @RequestBody Tournament tournament)
    {
        tournamentService.addTournament(tournament);
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping(path = "{id}")
    public Tournament getTournamentById(@PathVariable("id") UUID id)
    {
        return tournamentService.getTournamentById(id)
                .orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deleteTournamentById(@PathVariable("id") UUID id)
    {
        tournamentService.deleteTournament(id);
    }

    @PutMapping(path = "{id}")
    public void updateTournamentById(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody Tournament tournamentToUpdate)
    {
        tournamentService.updateTournament(id, tournamentToUpdate);
    }
}
