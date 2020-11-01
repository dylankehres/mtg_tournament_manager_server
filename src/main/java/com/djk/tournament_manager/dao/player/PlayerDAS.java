package com.djk.tournament_manager.dao.player;

import com.djk.tournament_manager.dao.base.FirebaseDAO;
import com.djk.tournament_manager.model.Player;
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

@Repository("firebasePlayerDao")
public class PlayerDAS extends FirebaseDAO<Player> implements PlayerDAO {
    static final String collection = "player";

    public PlayerDAS() {
        super(collection, Player.class);
    }

    @Override
    public ArrayList<Player> selectPlayersByTournamentCode(String code) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("roomCode", code).get();
        ArrayList<Player> playersInTournament = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for (QueryDocumentSnapshot queryDocumentSnapshot : docList) {
                    playersInTournament.add(queryDocumentSnapshot.toObject(Player.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        playersInTournament.sort(Comparator.comparing(Player::getPoints).reversed());

        return playersInTournament;
    }

    public ArrayList<Player> selectPlayersByTournamentID (String tmtID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentID", tmtID).get();
        ArrayList<Player> playersInTournament = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for (QueryDocumentSnapshot queryDocumentSnapshot : docList) {
                    playersInTournament.add(queryDocumentSnapshot.toObject(Player.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return playersInTournament;
    }
}
