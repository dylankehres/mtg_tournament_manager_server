package com.djk.tournament_manager.service;

import com.djk.tournament_manager.dao.TournamentDao;
import com.djk.tournament_manager.model.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}
