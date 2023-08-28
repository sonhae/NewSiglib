package com.signition.siglib.repository.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.signition.siglib.repository.RepositoryProxy;
import jakarta.persistence.EntityManagerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class RepositoryProxyProvider<T> implements Provider<Object> {
    private final Class<?> repositoryInterface;

    @Inject
    public RepositoryProxyProvider(Class<?> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public Object get() {
        InvocationHandler handler = new RepositoryProxy<>(repositoryInterface);
        return  Proxy.newProxyInstance(repositoryInterface.getClassLoader(),
                new Class[]{repositoryInterface},
                handler);
    }
}
