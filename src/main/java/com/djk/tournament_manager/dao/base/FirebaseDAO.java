package com.djk.tournament_manager.dao.base;

import com.djk.tournament_manager.model.BaseModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class FirebaseDAO<T extends BaseModel> implements BaseDAO<T> {
    private final String collection;
    private final Class<T> modelClass;

    public FirebaseDAO(String collection, Class<T> modelClass) {
        this.collection = collection;
        this.modelClass = modelClass;
    }

    @Override
    public T insert(T model) {
        UUID id = UUID.randomUUID();
        model.setID(id.toString());
        return insert(id.toString(), model);
    }

    @Override
    public T insert(String id, T model) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id).set(model);

        try {
            collectionApiFuture.get();
            if (collectionApiFuture.isDone()) {
                return model;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(collection).document(id).delete();
    }

    @Override
    public T update(T model) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(model.getID()).set(model);

        try {
            collectionApiFuture.get();
            if (collectionApiFuture.isDone()) {
                return model;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ArrayList<T> selectAll() {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> querySnapshot = dbFirestore.collection(collection).get();
        ArrayList<T> selectedModels = new ArrayList<>();

        try {
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                selectedModels.add(document.toObject(this.modelClass));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return selectedModels;
    }

    @Override
    public T selectById(String id) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(collection).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if(document != null && document.exists()){
            return document.toObject(this.modelClass);
        }

        return null;
    }
}
