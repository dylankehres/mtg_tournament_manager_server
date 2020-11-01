package com.djk.tournament_manager.dao.tournament;

import com.djk.tournament_manager.model.Tournament;

import java.util.ArrayList;

public interface TournamentDAO {
    Tournament insert(String id, Tournament model);

    Tournament insert(Tournament model);

    void deleteById(String id);

    Tournament update(Tournament model);

    ArrayList<Tournament> selectAll();

    Tournament selectById(String id);

     Tournament selectTournamentByCode(String code);
}
