package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Tournament;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("firebaseTournamentDaoNew")
public class TournamentDAS_NEW extends FirebaseDAO<Tournament> implements TournamentDAO_NEW {
    static final String collection = "tournament";

    @Override
    public List<Tournament> selectAllTournaments() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).get();
        List<Tournament> selectedTournaments = new ArrayList();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                selectedTournaments.add(document.toObject(Tournament.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return selectedTournaments;
    }

    @Override
    public Tournament selectTournamentById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(collection).document(id.toString());
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        Tournament tournament = null;
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if(document.exists()){
            tournament = document.toObject(Tournament.class);
            return tournament;
        }

        return null;
    }

    @Override
    public Tournament selectTournamentByCode(String code) {
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("roomCode", code).get();

        List<QueryDocumentSnapshot> docList = null;
        try {
            docList = querySnapshot.get().getDocuments();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(!docList.isEmpty()){
            return docList.get(0).toObject(Tournament.class);
        }

        return null;
    }
}
