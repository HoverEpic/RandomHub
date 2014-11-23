package me.jaime29010.randomhub;

import java.util.List;

public class RHConfig {

    RHPlugin plugin;

    public RHConfig(RHPlugin plugin) {
        this.plugin = plugin;
    }

    public List<String> getServersList() {
        return plugin.getConfigInstance().getStringList("servers_list");
    }

    public boolean isPrefixEnabled() {
        return plugin.getConfigInstance().getBoolean("prefix_enabled");
    }

    public String getPrefix() {
        return plugin.getConfigInstance().getString("prefix");
    }

    public boolean isCommandEnabled() {
        return plugin.getConfigInstance().getBoolean("command_enabled");
    }

    public String getCommand() {
        return plugin.getConfigInstance().getString("command");
    }

    public boolean isCommandPermissionEnabled() {
        return plugin.getConfigInstance().getBoolean("command_permission_enabled");
    }

    public String getPermission() {
        if(isCommandPermissionEnabled()) {
            return plugin.getConfigInstance().getString("command_permission");
        } else {
            return "";
        }
    }

    public String[] getCommandAliases() {
        return plugin.getConfigInstance().getStringList("command_aliases").toArray(new String[plugin.getConfigInstance().getStringList("command_aliases").size()]);
    }

    public String getCommandMessage() {
        return plugin.getConfigInstance().getString("command_message");
    }

    public String getAlreadyConnectedMessage() {
        return plugin.getConfigInstance().getString("already_connected_message");
    }

    public String getCantConnectMessage() {
        return plugin.getConfigInstance().getString("cant_connect_message");
    }

    public boolean isJoinMessageEnabled() {
        return plugin.getConfigInstance().getBoolean("join_message_enabled");
    }

    public String getJoinMessage() {
        return plugin.getConfigInstance().getString("join_message");
    }
}