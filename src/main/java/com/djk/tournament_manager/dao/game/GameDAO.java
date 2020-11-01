package com.djk.tournament_manager.dao.game;

import com.djk.tournament_manager.model.Game;

import java.util.ArrayList;

public interface GameDAO {
    Game insert(String id, Game game);

    Game insert(Game game);

    ArrayList<Game> selectAll();

    ArrayList<Game> selectGamesInMatch(String matchID);

    ArrayList<Game> selectGamesInTournament(String tournamentID);

    Game selectById(String id);

    void deleteById(String id);

    Game update(Game game);
}
