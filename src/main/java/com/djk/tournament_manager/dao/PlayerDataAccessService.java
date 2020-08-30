package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Player;
import com.djk.tournament_manager.model.Tournament;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Repository("firebasePlayerDao")
public class PlayerDataAccessService implements PlayerDao{
    static final String collection = "player";

    @Override
    public Player insertPlayer(String id, String tournamentID, String name, String roomCode, String format, String deckName) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Player newPlayer = new Player(id, tournamentID, name, roomCode, format, deckName);
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id.toString()).set(newPlayer);

        try {
            WriteResult res = collectionApiFuture.get();

            if (collectionApiFuture.isDone()) {
                return newPlayer;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new Player();
    }

    @Override
    public List<Player> selectAllPlayers() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).get();
        List<Player> selectedPlayers = new ArrayList();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                selectedPlayers.add(document.toObject(Player.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return selectedPlayers;
    }

    @Override
    public List<Player> selectPlayersByTournament(String code) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("roomCode", code).get();
        List<Player> playersInTournament = new ArrayList();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for(int i = 0; i<docList.size(); i++) {
                    playersInTournament.add(docList.get(i).toObject(Player.class));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return playersInTournament;
    }

    public List<Player> selectPlayersByTournamentID (String tmtID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentID", tmtID).get();
        List<Player> playersInTournament = new ArrayList();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for(int i = 0; i<docList.size(); i++) {
                    playersInTournament.add(docList.get(i).toObject(Player.class));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return playersInTournament;
    }

    @Override
    public Player selectPlayerById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(collection).document(id.toString());
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        Player player = null;
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if(document.exists()){
            player = document.toObject(Player.class);
            return player;
        }

        return new Player();
    }

    @Override
    public void deletePlayerById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id).delete();
    }

    @Override
    public void updatePlayerById(String id, Player player) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id.toString()).set(player);
    }
}
