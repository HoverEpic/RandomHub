package me.jaime29010.randomhub;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class RHConfig {

    RHPlugin plugin;
    Configuration config;

    public RHConfig(RHPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.printInfo(Level.SEVERE, "An error occurred while loading the config");
            e.printStackTrace();
        }
    }

    public List<String> getServersList() {
        return config.getStringList("servers_list");
    }

    public boolean isPrefixEnabled() {
        return config.getBoolean("prefix_enabled");
    }

    public String getPrefix() {
        return config.getString("prefix_label");
    }

    public boolean isCommandPermissionEnabled() {
        return config.getBoolean("command_permission_enabled");
    }

    public String getPermission() {
        if(isCommandPermissionEnabled()) {
            return config.getString("command_permission_string");
        } else {
            return "";
        }
    }

    public String[] getCommandAliases() {
        return config.getStringList("command_aliases").toArray(new String[config.getStringList("command_aliases").size()]);
    }

    public String getCommandMessage() {
        return config.getString("command_message");
    }

    public String getAlreadyConnectedMessage() {
        return config.getString("already_connected_message");
    }

    public boolean isJoinMessageEnabled() {
        return config.getBoolean("join_message_enabled");
    }

    public String getJoinMessage() {
        return config.getString("join_message_string");
    }
}
