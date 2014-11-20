package me.jaime29010.randomhub;

import me.jaime29010.randomhub.utils.Metrics;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class RHPlugin extends Plugin {

    RHConfig config;

    @Override
    public void onEnable() {
        config.load();
        config = new RHConfig(this);
        getProxy().getPluginManager().registerListener(this, new RHListener(this));
    }

    @Override
    public void onDisable() {
        config = null;
    }

    public void reloadPlugin() {
        config.load();
    }

    public void startMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
            printInfo(Level.INFO, "Metrics was successfully enabled");
        } catch (IOException e) {
            printInfo(Level.SEVERE, "An error occurred while enabling Metrics");
            e.getStackTrace();
        }
    }

    public RHConfig getConfig() {
        return config;
    }

    public void printInfo(Level level, String info) {
        getLogger().log(level, info);
    }
}
