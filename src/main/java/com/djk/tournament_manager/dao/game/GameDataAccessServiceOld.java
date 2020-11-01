package com.djk.tournament_manager.dao.game;

import com.djk.tournament_manager.model.Game;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("firebaseGameDaoOld")
public class GameDataAccessServiceOld implements GameDaoOld {
    static final String collection = "game";

    @Override
    public Game insert(String id, String matchID, String tournamentID, int gameNum) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Game newGame = new Game(matchID, tournamentID, gameNum);
        newGame.setID(id);
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id.toString()).set(newGame);

        try {
            WriteResult res = collectionApiFuture.get();

            if (collectionApiFuture.isDone()) {
                return newGame;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new Game();
    }

    @Override
    public List<Game> selectAll() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).get();
        List<Game> selectedGames = new ArrayList();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                selectedGames.add(document.toObject(Game.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return selectedGames;
    }

    @Override
    public List<Game> selectGamesInMatch(String matchID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("matchID", matchID).get();
        List<Game> gamesInMatch = new ArrayList();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for(int i = 0; i<docList.size(); i++) {
                    gamesInMatch.add(docList.get(i).toObject(Game.class));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        gamesInMatch.sort(new Comparator<Game>() {
//            @Override
//            public int compare(Game g1, Game g2) {
//                return g1.getGameNum() - g2.getGameNum();
//            }
//        });
        gamesInMatch.sort(Comparator.comparing(Game::getGameNum));

        return gamesInMatch;
    }

    @Override
    public List<Game> selectGamesInTournament(String tournamentID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentID", tournamentID).get();
        List<Game> gamesInMatch = new ArrayList();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for(int i = 0; i<docList.size(); i++) {
                    gamesInMatch.add(docList.get(i).toObject(Game.class));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return gamesInMatch;
    }

    @Override
    public Game selectById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(collection).document(id.toString());
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        Game game = null;
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(document.exists()){
            game = document.toObject(Game.class);
            return game;
        }

        return null;
    }

    @Override
    public void deleteById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id).delete();

    }

    @Override
    public Game update(Game game) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(game.getID()).set(game);

        try {
            WriteResult res = collectionApiFuture.get();

            if (collectionApiFuture.isDone()) {
                return game;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new Game();
    }
}
