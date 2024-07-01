package me.lucwsh.blizzardsync.commands;

import me.lucwsh.blizzardsync.managers.FilesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(FilesManager.messages.getString("messages.basic.console-cannot").replace("&", "§"));
        }



        return true;
    }
}
