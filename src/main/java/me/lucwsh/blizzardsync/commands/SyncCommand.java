package me.lucwsh.blizzardsync.commands;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.inventories.SyncInventory;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.managers.LoadersManager;
import me.lucwsh.blizzardsync.utils.PermissionChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SyncCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player player = null;
        boolean isConsole = false;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            isConsole = true;
        }

        String consoleError = FilesManager.messages.getString("messages.basic.console-cannot").replace("&", "§");

        if (args.length == 0) {
            if (isConsole) {
                sender.sendMessage(consoleError);
                return true;
            }

            if (!PermissionChecker.hasPermission(player, "permissions.sync.use")) {
                return true;
            }

            SyncInventory.open(player);

            ArrayList<String> opened = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.open-menu"));
            opened.replaceAll(line -> line.replace("&", "§"));

            for (String string : opened) {
                sender.sendMessage(string);
            }

        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!isConsole && !PermissionChecker.hasPermission(player, "permissions.sync.operator")) {
                    return true;
                }

                FilesManager.registerFiles();
                Main.instance.reloadConfig();
                LoadersManager.registerConfig();

                ArrayList<String> reload = new ArrayList<>(FilesManager.messages.getStringList("messages.basic.reload"));
                reload.replaceAll(line -> line.replace("&", "§"));

                for (String string : reload) {
                    sender.sendMessage(string);
                }
            }
        }
        return true;
    }
}
