package me.jaime29010.randomhub;

import com.google.common.io.ByteStreams;
import me.jaime29010.randomhub.commands.RHCommand;
import me.jaime29010.randomhub.commands.RHConnectCommand;
import me.jaime29010.randomhub.utils.Metrics;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class RHPlugin extends Plugin {

    ArrayList<String> servers = new ArrayList<String>();

    RHConnectCommand connectCommand;

    Configuration config;

    @Override
    public void onEnable() {
        loadConfig();
        enablePlugin();
    }

    @Override
    public void onDisable() {
        disablePlugin();
    }

    public void enablePlugin() {
        startMetrics();

        getProxy().getPluginManager().registerListener(this, new RHListener(this));
        getProxy().getPluginManager().registerCommand(this, new RHCommand(this));


        if(getConfig().getBoolean("command_enabled")) {
            getProxy().getPluginManager().registerCommand(this, connectCommand);
        }
    }

    public void disablePlugin() {
        config = null;
    }

    public void loadConfig() {
        saveDefaultConfig();

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            printInfo(Level.SEVERE, "An error occurred while loading the config");
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {
                Files.copy(getResourceAsStream("config.yml"), file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reloadPlugin() {
        loadConfig();

        if (getConfig().getBoolean("command_enabled")) {
            getProxy().getPluginManager().unregisterCommand(connectCommand);
            getProxy().getPluginManager().registerCommand(this, connectCommand);
        }

        listServers();
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

    public Configuration getConfig() {
        return config;
    }

    public ServerInfo getRandomServer() {
        return getProxy().getServerInfo(getServersList().get(new SecureRandom().nextInt(getServersList().size())));
    }

    public List<String> getServersList() {
        return servers;
    }

    public void listServers() {
        servers.clear();

        servers.addAll(getConfig().getStringList("servers_list"));

        if(getConfig().getBoolean("prefix_enabled")) {
            for(ServerInfo server : getProxy().getServers().values()) {
                if(server.getName().startsWith(getConfig().getString("prefix"))) {
                    if(servers.contains(server.getName())) {
                        printInfo(Level.INFO, "Found a server already on the config list (" + server.getName() + "), ignoring it and using the provided one.");
                    } else  {
                        servers.add(server.getName());
                        printInfo(Level.INFO, "Added the server " + server.getName() + " to the internal server list.");
                    }
                }
            }
        }

        printInfo(Level.INFO, "Added " + getServersList().size() + " to the servers list.");
    }

    public String[] getAliases() {
        return getConfig().getStringList("command_aliases").toArray(new String[getConfig().getStringList("command_aliases").size()]);
    }

    public void printInfo(Level level, String info) {
        getLogger().log(level, info);
    }
}