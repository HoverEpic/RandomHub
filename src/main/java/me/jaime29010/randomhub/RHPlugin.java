package me.jaime29010.randomhub;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

public class RHPlugin extends Plugin {

    Configuration config;

    RHConfig configManager;

    @Override
    public void onEnable() {
        config = null;
        configManager = new RHConfig(this);
        getProxy().getPluginManager().registerListener(this, new RHListener(this));
    }

    @Override
    public void onDisable() {
        config = null;
    }

    public void loadConfig() {

    }

    public void reloadPlugin() {

    }

    public void startMetrics() {

    }

    public Configuration getConfig() {
        return config;
    }
}
