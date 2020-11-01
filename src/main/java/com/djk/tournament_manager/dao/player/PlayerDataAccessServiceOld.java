package com.djk.tournament_manager.dao.player;

import com.djk.tournament_manager.model.Player;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository("firebasePlayerDaoOld")
public class PlayerDataAccessServiceOld implements PlayerDaoOld {
    static final String collection = "player";

    @Override
    public Player insert(String id, Player player) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id).set(player);

        try {
            WriteResult res = collectionApiFuture.get();

            if (collectionApiFuture.isDone()) {
                return player;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new Player();
    }

    @Override
    public ArrayList<Player> selectAll() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).get();
        ArrayList<Player> selectedPlayers = new ArrayList();

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
    public ArrayList<Player> selectPlayersByTournamentCode(String code) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("roomCode", code).get();
        ArrayList<Player> playersInTournament = new ArrayList();

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

        playersInTournament.sort(Comparator.comparing(Player::getPoints).reversed());

        return playersInTournament;
    }

    public ArrayList<Player> selectPlayersByTournamentID (String tmtID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentID", tmtID).get();
        ArrayList<Player> playersInTournament = new ArrayList();

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
    public Player selectById(String id) {
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
    public void deleteById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id).delete();
    }

    @Override
    public Player update(Player player) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(player.getID()).set(player);

        try {
            WriteResult res = collectionApiFuture.get();

            if (collectionApiFuture.isDone()) {
                return player;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new Player();
    }
}
