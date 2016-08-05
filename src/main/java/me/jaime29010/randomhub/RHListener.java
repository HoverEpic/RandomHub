package me.jaime29010.randomhub;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Level;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class RHListener implements Listener
{

    RHPlugin m_Instance;

    public RHListener(RHPlugin plugin)
    {
        this.m_Instance = plugin;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent p_Event)
    {
        // check if player is not already connected on a server
        if ((p_Event.getPlayer().getServer() == null) && !p_Event.isCancelled())
        {
            //check if the randomhub config contains the target
            if (m_Instance.getServersList().contains(p_Event.getTarget().getName()))
            {
                sendPlayerToRandomServer(p_Event);

            } else
            {
                p_Event.getPlayer().sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', m_Instance.getConfigGetter().getCantConnectMessage())).color(ChatColor.RED).create());
            }
        }
    }

    private boolean ping(ServerInfo p_ServerInfo)
    {
        Socket socket = new Socket();
        try
        {
            SocketAddress socketAddress = p_ServerInfo.getAddress();
            socket.connect(socketAddress, 2000);
            socket.close();
            return true;
        } catch (IOException e)
        {
            m_Instance.printInfo(Level.INFO, "Ping server " + p_ServerInfo.getName() + " failed!");
            return false;
        }

    }

    public boolean isAccessible(ServerInfo p_ServerInfo, ProxiedPlayer p_ProxiedPlayer)
    {
        if (p_ServerInfo.canAccess(p_ProxiedPlayer))
        {
            if (ping(p_ServerInfo))
            {
                return true;
            }
        }
        return false;
    }

    public void sendPlayerToRandomServer(ServerConnectEvent p_Event)
    {
        ServerInfo l_Target = m_Instance.getRandomServer();
        if (isAccessible(l_Target, p_Event.getPlayer()))
        {
            p_Event.setCancelled(true);

            m_Instance.printInfo(Level.INFO, "Preparing player " + p_Event.getPlayer().getName() + " to be sent on a random server!");

            try
            {
                m_Instance.printInfo(Level.INFO, "Sending player " + p_Event.getPlayer().getName() + " to server " + l_Target.getName());
                p_Event.setTarget(l_Target);
                p_Event.setCancelled(false);
                return;
            } catch (Exception e)
            {
                m_Instance.printInfo(Level.SEVERE, "The plugin failed, that is because you have put servers that are offline on the config.");
            }
        }
        sendPlayerToRandomServer(p_Event);
    }

    @EventHandler
    public void onServerConnected(ServerConnectedEvent event)
    {
        if (m_Instance.getConfigGetter().isJoinMessageEnabled() && m_Instance.getServersList().contains(event.getServer().getInfo().getName()))
        {
            event.getPlayer().sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', m_Instance.getConfigGetter().getJoinMessage().replaceAll("%servername%", event.getServer().getInfo().getName()))).create());
        }
    }
}
