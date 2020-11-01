package com.djk.tournament_manager.dao.game;

import com.djk.tournament_manager.model.Game;

import java.util.List;
import java.util.UUID;

public interface GameDaoOld {
    Game insert(String id, String matchID, String tournamentID, int gameNum);

    default Game insert(String matchID, String tournamentID, int gameNum) {
        UUID id = UUID.randomUUID();
        return insert(id.toString(), matchID, tournamentID, gameNum);
    }

    List<Game> selectAll();

    List<Game> selectGamesInMatch(String matchID);

    List<Game> selectGamesInTournament(String tournamentID);

    Game selectById(String id);

    void deleteById(String id);

    Game update(Game game);
}
