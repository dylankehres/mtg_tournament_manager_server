package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.TournamentDao;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TournamentService {

    private final TournamentDao tournamentDao;

    @Autowired
    public TournamentService(@Qualifier("fakeDao") TournamentDao tournamentDao)
    {
        this.tournamentDao = tournamentDao;
    }

    public int addTournament(Tournament tournament)
    {
        return tournamentDao.insertTournament(tournament);
    }

    public List<Tournament> getAllTournaments()
    {
        return tournamentDao.selectAllTournaments();
    }

    public Optional<Tournament> getTournamentById(UUID id)
    {
        return tournamentDao.selectTournamentById(id);
    }

    public int deleteTournament(UUID id) {
        return tournamentDao.deleteTournamentById(id);
    }

    public int updateTournament(UUID id, Tournament tournament)
    {
        return tournamentDao.updateTournamentById(id, tournament);
    }

}
