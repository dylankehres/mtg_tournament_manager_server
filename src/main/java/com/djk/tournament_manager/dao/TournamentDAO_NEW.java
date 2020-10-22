package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.BaseModel;
import com.djk.tournament_manager.model.Tournament;

import java.util.List;
import java.util.UUID;

public interface TournamentDAO_NEW {
    Tournament insert(String id, Tournament model);

    Tournament insert(Tournament model);

    void deleteById(String id);

    Tournament update(Tournament model);

    List<Tournament> selectAllTournaments();

    Tournament selectTournamentById(String id);

     Tournament selectTournamentByCode(String code);
}
