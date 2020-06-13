package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.Match;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("firebaseMatchDao")
public class MatchDataAccessService implements MatchDao {
    static final String collection = "match";

    @Override
    public Match insertMatch(String id, String tournamentID, int numGames, String player1ID, String player2ID, int tableNum) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Match newMatch = new Match(id, tournamentID, numGames, player1ID, player2ID, tableNum);
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id.toString()).set(newMatch);
        return newMatch;
    }

    @Override
    public List<Match> selectAllMatches() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).get();
        List<Match> selectedMatches = new ArrayList();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                selectedMatches.add(document.toObject(Match.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return selectedMatches;
    }

    @Override
    public List<Match> selectMatchesInTournament(String tournamentID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentID", tournamentID).get();
        List<Match> matchesInTournament = new ArrayList();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for(int i = 0; i<docList.size(); i++) {
                    matchesInTournament.add(docList.get(i).toObject(Match.class));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return matchesInTournament;
    }

    @Override
    public Match selectMatchById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(collection).document(id.toString());
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        Match match = null;
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(document.exists()){
            match = document.toObject(Match.class);
            return match;
        }

        return null;
    }

    @Override
    public Match selectMatchByPlayerID(String playerId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotP1 = dbFirestore.collection(collection).whereEqualTo("player1ID", playerId).get();
        ApiFuture<QuerySnapshot> querySnapshotP2 = dbFirestore.collection(collection).whereEqualTo("player2ID", playerId).get();
        Match match = null;

        try {
            List<QueryDocumentSnapshot> docList = new ArrayList<>();
            List<QueryDocumentSnapshot> player1MatchList = querySnapshotP1.get().getDocuments();
            List<QueryDocumentSnapshot> player2MatchList = querySnapshotP2.get().getDocuments();

            docList.addAll(player1MatchList);
            docList.addAll(player2MatchList);

            if(!docList.isEmpty()){
               return docList.get(0).toObject(Match.class);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteMatchById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id).delete();
    }

    @Override
    public void updateMatch(Match match) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(match.getID()).set(match);
    }
}
