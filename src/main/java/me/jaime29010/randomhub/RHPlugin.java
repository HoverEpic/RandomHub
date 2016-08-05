package me.jaime29010.randomhub;

import me.jaime29010.randomhub.commands.RHCommand;
import me.jaime29010.randomhub.commands.RHConnectCommand;
import me.jaime29010.randomhub.utils.Metrics;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class RHPlugin extends Plugin
{

    ArrayList<String> servers = new ArrayList<>();

    RHConnectCommand connectCommand;
    RHConfig configGetter;

    Configuration configInstance;

    @Override
    public void onEnable()
    {
        loadConfig("config.yml");

        configGetter = new RHConfig(this);
        connectCommand = new RHConnectCommand(this);

        startMetrics();

        getProxy().getPluginManager().registerListener(this, new RHListener(this));
        getProxy().getPluginManager().registerCommand(this, new RHCommand(this));

        if (getConfigGetter().isCommandEnabled())
        {
            getProxy().getPluginManager().registerCommand(this, connectCommand);
        }

        listServers();
    }

    @Override
    public void onDisable()
    {
        configInstance = null;
        configGetter = null;
        connectCommand = null;
    }

    public void loadConfig(String resource)
    {
        if (!getDataFolder().exists())
        {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), resource);

        if (!file.exists())
        {
            try
            {
                Files.copy(getResourceAsStream(resource), file.toPath());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            configInstance = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), resource));
        } catch (IOException e)
        {
            printInfo(Level.SEVERE, "An error occurred while loading the config");
            e.printStackTrace();
        }
    }

    public void reloadPlugin()
    {
        if (getConfigGetter().isCommandEnabled())
        {
            getProxy().getPluginManager().unregisterCommand(connectCommand);
        }

        loadConfig("config.yml");

        if (getConfigGetter().isCommandEnabled())
        {
            getProxy().getPluginManager().registerCommand(this, connectCommand);
        }

        listServers();
    }

    public void startMetrics()
    {
        try
        {
            Metrics metrics = new Metrics(this);
            metrics.start();
            printInfo(Level.INFO, "Metrics was successfully enabled");
        } catch (IOException e)
        {
            printInfo(Level.SEVERE, "An error occurred while enabling Metrics");
            e.getStackTrace();
        }
    }

    public Configuration getConfigInstance()
    {
        return configInstance;
    }

    public RHConfig getConfigGetter()
    {
        return configGetter;
    }

    public ServerInfo getRandomServer()
    {
        return getProxy().getServerInfo(getServersList().get(new SecureRandom().nextInt(getServersList().size())));
    }

    public List<String> getServersList()
    {
        return servers;
    }

    public void listServers()
    {
        servers.clear();

        servers.addAll(getConfigGetter().getServersList());

        if (getConfigGetter().isPrefixEnabled())
        {
            for (ServerInfo server : getProxy().getServers().values())
            {
                if (server.getName().startsWith(getConfigGetter().getPrefix()))
                {
                    if (servers.contains(server.getName()))
                    {
                        printInfo(Level.INFO, "Found a server already on the config list (" + server.getName() + "), ignoring it and using the provided one.");
                    } else
                    {
                        servers.add(server.getName());
                        printInfo(Level.INFO, "Added the server " + server.getName() + " to the internal server list.");
                    }
                }
            }
        }

        printInfo(Level.INFO, "Added " + getServersList().size() + " to the servers list.");
    }

    public void printInfo(Level level, String info)
    {
        getLogger().log(level, info);
    }
}
