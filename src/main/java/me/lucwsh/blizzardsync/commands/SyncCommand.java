package me.lucwsh.blizzardsync.commands;

import me.lucwsh.blizzardsync.inventories.SyncInventory;
import me.lucwsh.blizzardsync.managers.FilesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SyncCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(FilesManager.messages.getString("messages.basic.console-cannot").replace("&", "§"));
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            SyncInventory.open(player);
        } else {
            return true;
        }

        return true;
    }
}
