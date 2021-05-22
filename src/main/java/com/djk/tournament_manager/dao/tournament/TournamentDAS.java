package com.djk.tournament_manager.dao.tournament;

import com.djk.tournament_manager.dao.base.FirebaseDAO;
import com.djk.tournament_manager.model.Tournament;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("firebaseTournamentDao")
public class TournamentDAS extends FirebaseDAO<Tournament> implements TournamentDAO {
    static final String collection = "tournament";

    public TournamentDAS() {
        super(collection, Tournament.class);
    }

    @Override
    public ArrayList<Tournament> selectTournamentByStatus(int status) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentStatus", status).get();
        ArrayList<Tournament> selectedTournaments = new ArrayList<>();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                selectedTournaments.add(document.toObject(Tournament.class));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return selectedTournaments;
    }

    @Override
    public Tournament selectTournamentByCode(String code) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("roomCode", code).get();

        List<QueryDocumentSnapshot> docList = null;
        try {
            docList = querySnapshot.get().getDocuments();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if(docList != null && !docList.isEmpty()){
            return docList.get(0).toObject(Tournament.class);
        }

        return null;
    }
}
