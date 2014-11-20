package me.jaime29010.randomhub.commands;

import me.jaime29010.randomhub.RHPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class RHCommand extends Command {
    private RHPlugin plugin;

    public RHCommand(RHPlugin plugin) {
        super("randomhub");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("Currently ").color(ChatColor.GRAY).append(String.valueOf(plugin.getServersList().size())).color(ChatColor.RED).append(" servers are setup.").color(ChatColor.GRAY).create());
            sender.sendMessage(new ComponentBuilder("This plugin is on the version ").color(ChatColor.GRAY).append(plugin.getDescription().getVersion()).color(ChatColor.RED).append(" Coded by jaime29010").color(ChatColor.GRAY).create());
            sender.sendMessage(new ComponentBuilder("More information: /randomhub help").color(ChatColor.GRAY).create());

            TextComponent message = new TextComponent("Available Here");
            message.setColor(ChatColor.YELLOW);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://goo.gl/Zg9Xa7"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Go to the RandomHub's Thread!").create()));
            sender.sendMessage(message);
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(new ComponentBuilder("You can use the following commands: ").color(ChatColor.GRAY).create());
                sender.sendMessage(new ComponentBuilder("-/randomhub reload | Reloads the plugin config and the commands").color(ChatColor.GRAY).create());
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("randomhub.reload")) {
                        try {
                            plugin.getConfig().load();
                            sender.sendMessage(new ComponentBuilder("RandomHub was successfully reloaded!").color(ChatColor.GREEN).create());
                        } catch (IOException e) {
                            sender.sendMessage(new ComponentBuilder("Error trying to read the config file, send this error to the creator.").color(ChatColor.RED).create());
                        }
                        plugin.reloadPlugin();
                    } else {
                        sender.sendMessage(new ComponentBuilder("You do not have permission to execute this command!").color(ChatColor.RED).create());
                    }
                } else {
                    sender.sendMessage(new ComponentBuilder("This is not a valid argument for this command!").color(ChatColor.RED).create());
                }
            }
        }
    }
}
