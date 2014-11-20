package me.jaime29010.randomhub.commands;

import me.jaime29010.randomhub.RHPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class RHConnectCommand extends Command {
    RHPlugin plugin;
    public RHConnectCommand(RHPlugin  plugin) {
        super("");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }
}
