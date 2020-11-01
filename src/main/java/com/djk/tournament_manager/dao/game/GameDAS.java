package com.djk.tournament_manager.dao.game;

import com.djk.tournament_manager.dao.base.FirebaseDAO;
import com.djk.tournament_manager.model.Game;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("firebaseGameDao")
public class GameDAS extends FirebaseDAO<Game> implements GameDAO {
    static final String collection = "game";

    public GameDAS() {
        super(collection, Game.class);
    }

    @Override
    public List<Game> selectGamesInMatch(String matchID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("matchID", matchID).get();
        List<Game> gamesInMatch = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for (QueryDocumentSnapshot queryDocumentSnapshot : docList) {
                    gamesInMatch.add(queryDocumentSnapshot.toObject(Game.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        gamesInMatch.sort(Comparator.comparing(Game::getGameNum));

        return gamesInMatch;
    }

    @Override
    public List<Game> selectGamesInTournament(String tournamentID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentID", tournamentID).get();
        List<Game> gamesInMatch = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for (QueryDocumentSnapshot queryDocumentSnapshot : docList) {
                    gamesInMatch.add(queryDocumentSnapshot.toObject(Game.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return gamesInMatch;
    }
}
