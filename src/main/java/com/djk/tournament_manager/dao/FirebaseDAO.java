package com.djk.tournament_manager.dao;

import com.djk.tournament_manager.model.BaseModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class FirebaseDAO<T extends BaseModel> implements BaseDAO<T> {
    static final Firestore dbFirestore = FirestoreClient.getFirestore();
    static String collection = "";

    @Override
    public T insert(T model) {
        UUID id = UUID.randomUUID();
        return insert(id.toString(), model);
    }

    @Override
    public T insert(String id, T model) {
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(id.toString()).set(model);

        try {
            WriteResult res = collectionApiFuture.get();

            if (collectionApiFuture.isDone()) {
                return model;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collection).document(model.getID()).set(model);

        try {
            WriteResult res = collectionApiFuture.get();

            if (collectionApiFuture.isDone()) {
                return model;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
