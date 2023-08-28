package com.signition.siglib.module;

import com.google.inject.AbstractModule;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginModule extends AbstractModule {
    protected final JavaPlugin plugin;

    public PluginModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
    }


}
