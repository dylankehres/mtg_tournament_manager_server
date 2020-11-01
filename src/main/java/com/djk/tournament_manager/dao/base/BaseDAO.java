package com.djk.tournament_manager.dao.base;

import com.djk.tournament_manager.model.BaseModel;

import java.util.List;

public interface BaseDAO<T extends BaseModel> {
    T insert(String id, T model);

    T insert(T model);

    void deleteById(String id);

    T update(T model);

    List<T> selectAll();

    T selectById(String id);
}
