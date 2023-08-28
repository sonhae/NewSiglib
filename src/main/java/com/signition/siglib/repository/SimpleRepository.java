package com.signition.siglib.repository;

import com.signition.siglib.repository.annotations.Repository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Repository
public interface SimpleRepository<T,ID> {
    Single<T> findById(ID id);
    Completable save(T entity);
    Completable delete(T entity) ;
}
