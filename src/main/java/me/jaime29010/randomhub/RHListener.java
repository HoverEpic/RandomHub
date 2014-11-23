package me.jaime29010.randomhub;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Level;

public class RHListener implements Listener {
    RHPlugin plugin;

    public RHListener(RHPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if((event.getPlayer().getServer() == null) || (plugin.getServersList().contains(event.getTarget().getName()) && !plugin.getServersList().contains(event.getPlayer().getServer().getInfo().getName()))) {
            if (plugin.getServersList().contains(event.getTarget().getName())) {
                ServerInfo target = plugin.getRandomServer();
                if(target.canAccess(event.getPlayer())) {
                    try {
                        event.setTarget(plugin.getRandomServer());
                    } catch (Exception e) {
                        plugin.printInfo(Level.SEVERE, "The plugin failed, that is because you have put servers that are offline on the config.");
                    }
                } else {
                    event.getPlayer().sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', plugin.getConfigGetter().getCantConnectMessage())).color(ChatColor.RED).create());
                }
            }
        }
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event) {
        if (plugin.getConfigGetter().isJoinMessageEnabled() && plugin.getServersList().contains(event.getServer().getInfo().getName())) {
            event.getPlayer().sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', plugin.getConfigGetter().getJoinMessage().replaceAll("%servername%", event.getServer().getInfo().getName()))).create());
        }
    }
}