package com.djk.tournament_manager.dao.tournament;

import com.djk.tournament_manager.model.BaseModel;
import com.djk.tournament_manager.model.Tournament;

import java.util.List;
import java.util.UUID;

public interface TournamentDAO {
    Tournament insert(String id, Tournament model);

    Tournament insert(Tournament model);

    void deleteById(String id);

    Tournament update(Tournament model);

    List<Tournament> selectAllTournaments();

    Tournament selectTournamentById(String id);

     Tournament selectTournamentByCode(String code);
}
