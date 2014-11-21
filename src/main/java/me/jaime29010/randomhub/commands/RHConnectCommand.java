package me.jaime29010.randomhub.commands;

import me.jaime29010.randomhub.RHPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RHConnectCommand extends Command {

    RHPlugin plugin;

    public RHConnectCommand(RHPlugin plugin) {
        super(plugin.getConfig().getString("command"), plugin.getConfig().getString("command_permission"), plugin.getAliases());
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (!plugin.getServersList().contains(player.getServer().getInfo().getName())) {
                ServerInfo target = plugin.getRandomServer();
                sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("command_message").replaceAll("%servername%", target.getName()))).create());
                player.connect(target);
            } else {
                sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("already_connected_message"))).create());
            }
        } else {
            sender.sendMessage(new ComponentBuilder("This command can only be run by a player, you are a console.").color(ChatColor.RED).create());
        }
    }
}