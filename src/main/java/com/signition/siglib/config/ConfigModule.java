package com.signition.siglib.config;

import com.google.inject.Singleton;
import com.signition.siglib.config.annotations.Config;
import com.signition.siglib.module.PluginModule;
import com.signition.siglib.repository.annotations.Repository;
import com.signition.siglib.repository.provider.RepositoryProxyProvider;
import com.signition.siglib.util.ReflectionUtil;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

public class ConfigModule extends PluginModule {
    public ConfigModule(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void configure() {
        super.configure();

        ReflectionUtil.getAnnotatedClass(Config.class).forEach(c ->
                {
                    System.out.println("added " + c.getName());

                    String fileName = ((Class<?>)c).getAnnotation(Config.class).fileName();
                    bind(c).toProvider(new ConfigProvider(fileName, c)).in(Singleton.class);
                }
        );

    }
}
