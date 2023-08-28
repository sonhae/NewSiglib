package com.signition.siglib.repository;

import com.google.inject.Singleton;
import com.signition.siglib.module.PluginModule;
import com.signition.siglib.repository.annotations.Repository;
import com.signition.siglib.repository.provider.EntityManagerFactoryProvider;
import com.signition.siglib.repository.provider.RepositoryProxyProvider;
import jakarta.persistence.EntityManagerFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

public class RepositoryModule extends PluginModule {
    public RepositoryModule(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void configure() {
        super.configure();
        bind(EntityManagerFactory.class).toProvider(EntityManagerFactoryProvider.class);


        URLClassLoader classLoader = (URLClassLoader) this.plugin.getClass().getClassLoader();
        URL[] urls = classLoader.getURLs();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(urls)
                .setScanners(Scanners.TypesAnnotated));

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class repositoryInterface : annotated) {
            bind(repositoryInterface).toProvider(RepositoryProxyProvider.class).in(Singleton.class);
        }
    }
}
