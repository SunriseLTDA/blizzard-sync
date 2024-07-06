package me.lucwsh.blizzardsync.commands;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.discord.DiscordClient;
import me.lucwsh.blizzardsync.inventories.SyncInventory;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.managers.LoadersManager;
import me.lucwsh.blizzardsync.utils.PermissionChecker;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SyncCommand implements CommandExecutor {
    SyncAPI syncAPI = new SyncAPI();

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

        if (args.length > 3) {
            player.sendMessage("babaca");
            return true;
        }

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

        if (args.length == 2) {

            String action = args[0].toLowerCase();

            switch (action) {

                case "forceunsync":
                    Player forceUnSyncPlayer = Bukkit.getPlayer(args[1]);

                    if (forceUnSyncPlayer == null) {
                        return true;
                    }

                    syncAPI.unSyncUser(forceUnSyncPlayer);
                    player.sendMessage("" + forceUnSyncPlayer.getName());

                    break;

                default:
                    player.sendMessage("bruh");
            }
        }

        if (args.length == 3) {
            String action = args[0].toLowerCase();

            switch (action) {
                case "forcesync":
                    Player forceSyncPlayer = Bukkit.getPlayer(args[1]);
                    User forceSyncID = DiscordClient.jda.retrieveUserById(args[2]).complete();

                    if (forceSyncPlayer == null) {
                        return true;
                    }

                    if (forceSyncID == null) {
                        return true;
                    }

                    syncAPI.syncUser(forceSyncPlayer, forceSyncID.getId());
                    player.sendMessage("conseguiu conectar o " + forceSyncPlayer.getName() +  " à conta " + forceSyncID.getName());

                    break;

                default:
                    player.sendMessage("bruh");
            }
        }

        return true;
    }
}
