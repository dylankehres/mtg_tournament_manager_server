package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Game;
import com.djk.tournament_manager.model.Match;

import java.util.List;
import java.util.UUID;

public interface GameDao {
    Game insertGame(String id, String matchID, String tournamentID);

    default Game insertGame(String matchID, String tournamentID) {
        UUID id = UUID.randomUUID();
        return insertGame(id.toString(), matchID, tournamentID);
    }

    List<Game> selectAllGames();

    List<Game> selectGamesInMatch(String matchID);

    List<Game> selectGamesInTournament(String tournamentID);

    Game selectGameById(String id);

    void deleteGameById(String id);

    void updateGame(Game game);

}
