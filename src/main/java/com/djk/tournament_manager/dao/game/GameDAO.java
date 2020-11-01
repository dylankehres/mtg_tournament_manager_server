package com.djk.tournament_manager.dao.game;

import com.djk.tournament_manager.model.Game;

import java.util.List;

public interface GameDAO {
    Game insert(String id, Game game);

    Game insert(Game game);

    List<Game> selectAll();

    List<Game> selectGamesInMatch(String matchID);

    List<Game> selectGamesInTournament(String tournamentID);

    Game selectById(String id);

    void deleteById(String id);

    Game update(Game game);
}
