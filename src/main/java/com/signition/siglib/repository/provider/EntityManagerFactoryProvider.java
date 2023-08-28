package com.signition.siglib.repository.provider;

import com.google.inject.Provider;
import jakarta.persistence.EntityManagerFactory;

public class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
    @Override
    public EntityManagerFactory get() {
        return null;
    }
}
