package com.signition.siglib.config;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.signition.siglib.util.ConfigInjector;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;

public class ConfigProvider<T> implements Provider<T> {

    private final Class<T> configClass;
    private final String filename;

    @Inject
    private JavaPlugin plugin;

    public ConfigProvider(String filename, Class<T> configClass) {
        this.filename = filename;
        this.configClass = configClass;
    }


    @Override
    public T get() {
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filename)) {
            if (inputStream != null) {
                return ConfigInjector.injectConfig(configClass, inputStream);
            } else {
               throw new RuntimeException("file not found");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
