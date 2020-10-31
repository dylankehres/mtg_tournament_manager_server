package com.djk.tournament_manager.dao.base;

import com.djk.tournament_manager.model.BaseModel;
import com.djk.tournament_manager.model.Tournament;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import java.util.List;
import java.util.UUID;

public interface BaseDAO<T extends BaseModel> {
    public abstract T insert(String id, T model);

//    default T insert(T model) {
//        UUID id = UUID.randomUUID();
//        return insert(id.toString(), model);
//    }
    public abstract T insert(T model);

    public abstract void deleteById(String id);

    public abstract T update(T model);
}
