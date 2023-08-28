package com.signition.siglib.repository;


import com.google.inject.Inject;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class RepositoryProxy<T, ID> implements InvocationHandler {

    @Inject
    private EntityManagerFactory entityManagerFactory;

    private Class<T> entityType;

    public RepositoryProxy(Class<T> entityType) {
        this.entityType = entityType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        try {
            if (methodName.startsWith("findBy")) {
                String fieldName = methodName.substring(6);
                return findByField(fieldName, args[0]);
            } else if ("save".equals(methodName)) {
                return save((T) args[0]);
            } else if ("delete".equals(methodName)) {
                return delete((T) args[0]);
            } else if ("findById".equals(methodName)) {
                return findById((ID) args[0]);
            } else {
                return Completable.error(new UnsupportedOperationException("Method not supported"));
            }
        } catch (Exception e) {
            return Completable.error(e);
        }
    }

    private Single<List<T>> findByField(String fieldName, Object value) {
        return Single.fromCallable(() -> {
            EntityManager em = entityManagerFactory.createEntityManager();
            String hql = "FROM " + entityType.getName() + " E WHERE E." + fieldName + " = :value";
            List<T> results = em.createQuery(hql, entityType)
                    .setParameter("value", value)
                    .getResultList();
            em.close();
            return results;
        }).subscribeOn(Schedulers.io());
    }

    private Single<T> save(T entity) {
        return Single.fromCallable(() -> {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
            em.close();
            return entity;
        }).subscribeOn(Schedulers.io());
    }

    private Completable delete(T entity) {
        return Completable.fromRunnable(() -> {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
            em.close();
        }).subscribeOn(Schedulers.io());
    }

    private Single<T> findById(ID id) {
        return Single.fromCallable(() -> {
            EntityManager em = entityManagerFactory.createEntityManager();
            T result = em.find(entityType, id);
            em.close();
            return result;
        }).subscribeOn(Schedulers.io());
    }
}
