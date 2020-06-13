package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Game;
import com.djk.tournament_manager.model.Match;

import java.util.List;
import java.util.UUID;

public interface GameDao {
    Game insertGame(String id, String matchID);

    default Game insertGame(String matchID) {
        UUID id = UUID.randomUUID();
        return insertGame(id.toString(), matchID);
    }

    List<Game> selectAllGames();

    List<Game> selectGamesInMatch(String matchID);

    Game selectGameById(String id);

    void deleteGameById(String id);

    void updateGame(Game game);

}
