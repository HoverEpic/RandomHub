package me.jaime29010.randomhub;

import me.jaime29010.randomhub.commands.RHCommand;
import me.jaime29010.randomhub.commands.RHConnectCommand;
import me.jaime29010.randomhub.utils.Metrics;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class RHPlugin extends Plugin {

    ArrayList<String> servers = new ArrayList<>();

    RHConfig config;
    RHConnectCommand connectCommand;

    @Override
    public void onEnable() {
        config = new RHConfig(this);
        try {
            config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getProxy().getPluginManager().registerListener(this, new RHListener(this));
        getProxy().getPluginManager().registerCommand(this, new RHCommand(this));

        if(getConfig().isCommandEnabled()) {
            getProxy().getPluginManager().registerCommand(this, connectCommand);
        }

        startMetrics();
    }

    @Override
    public void onDisable() {
        config = null;
    }

    public void reloadPlugin() {
        if (getConfig().isCommandEnabled()) {
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

    public RHConfig getConfig() {
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

        servers.addAll(getConfig().getServersList());

        if(getConfig().isPrefixEnabled()) {
            for(ServerInfo server : getProxy().getServers().values()) {
                if(server.getName().startsWith(getConfig().getPrefix())) {
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

    public void printInfo(Level level, String info) {
        getLogger().log(level, info);
    }
}