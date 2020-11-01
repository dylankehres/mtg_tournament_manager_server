package com.djk.tournament_manager.dao.match;

import com.djk.tournament_manager.dao.base.FirebaseDAO;
import com.djk.tournament_manager.model.Match;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository("firebaseMatchDao")
public class MatchDAS extends FirebaseDAO<Match> implements MatchDAO {
    static final String collection = "match";

    public MatchDAS() {
        super(collection, Match.class);
    }

    @Override
    public List<Match> selectMatchesInTournament(String tournamentID) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).whereEqualTo("tournamentID", tournamentID).get();
        List<Match> matchesInTournament = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for (QueryDocumentSnapshot queryDocumentSnapshot : docList) {
                    matchesInTournament.add(queryDocumentSnapshot.toObject(Match.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return matchesInTournament;
    }

    @Override
    public List<Match> selectMatchesInRound(String tournamentID, int roundNum) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection)
                .whereEqualTo("tournamentID", tournamentID)
                .whereEqualTo("roundNum", roundNum)
                .get();
        List<Match> matchesInTournament = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> docList = querySnapshot.get().getDocuments();
            if(!docList.isEmpty()){
                for (QueryDocumentSnapshot queryDocumentSnapshot : docList) {
                    matchesInTournament.add(queryDocumentSnapshot.toObject(Match.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return matchesInTournament;
    }

    @Override
    public Match selectMatchByPlayerID(String playerId, int roundNum) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotP1 = dbFirestore.collection(collection)
                .whereEqualTo("player1ID", playerId)
                .whereEqualTo("roundNum", roundNum)
                .get();
        ApiFuture<QuerySnapshot> querySnapshotP2 = dbFirestore.collection(collection)
                .whereEqualTo("player2ID", playerId)
                .whereEqualTo("roundNum", roundNum)
                .get();

        try {
            List<QueryDocumentSnapshot> docList = new ArrayList<>();
            List<QueryDocumentSnapshot> player1MatchList = querySnapshotP1.get().getDocuments();
            List<QueryDocumentSnapshot> player2MatchList = querySnapshotP2.get().getDocuments();

            docList.addAll(player1MatchList);
            docList.addAll(player2MatchList);

            if(!docList.isEmpty()){
                return docList.get(0).toObject(Match.class);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<Match> selectAllMatchesByPlayerID(String playerId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshotP1 = dbFirestore.collection(collection)
                .whereEqualTo("player1ID", playerId)
                .get();
        ApiFuture<QuerySnapshot> querySnapshotP2 = dbFirestore.collection(collection)
                .whereEqualTo("player2ID", playerId)
                .get();
        ArrayList<Match> matches = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> docList = new ArrayList<>();
            List<QueryDocumentSnapshot> player1MatchList = querySnapshotP1.get().getDocuments();
            List<QueryDocumentSnapshot> player2MatchList = querySnapshotP2.get().getDocuments();

            docList.addAll(player1MatchList);
            docList.addAll(player2MatchList);

            if(!docList.isEmpty()){
                for (QueryDocumentSnapshot queryDocumentSnapshot : docList) {
                    matches.add(queryDocumentSnapshot.toObject(Match.class));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
