package me.lucwsh.blizzardsync.commands;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.discord.DiscordClient;
import me.lucwsh.blizzardsync.discord.roles.RolesUtils;
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
            ArrayList<String> playerUsage = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.use"));
            playerUsage.replaceAll(line -> line.replace("&", "§"));

            for (String string : playerUsage) {
                sender.sendMessage(string);
            }
            return true;
        }

        if (args.length == 0) {
            if (isConsole) {
                sender.sendMessage(consoleError);
                return true;
            }

            if (!PermissionChecker.hasPermission(player, FilesManager.permissions.getString("permissions.sync.use-sync"))) {
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
                if (!isConsole && !PermissionChecker.hasPermission(player, FilesManager.permissions.getString("permissions.sync.operator"))) {
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
            } else {
                ArrayList<String> playerUsage = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.use"));
                playerUsage.replaceAll(line -> line.replace("&", "§"));

                for (String string : playerUsage) {
                    sender.sendMessage(string);
                }
            }
        }

        if (args.length == 2) {
            if (!isConsole && !PermissionChecker.hasPermission(player, FilesManager.permissions.getString("permissions.sync.operator"))) {
                return true;
            }

            String action = args[0].toLowerCase();

            switch (action) {

                case "forceunsync":
                    Player forceUnSyncPlayer = Bukkit.getPlayer(args[1]);

                    if (forceUnSyncPlayer == null) {
                        ArrayList<String> notFoundPlayer = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.not-found"));
                        notFoundPlayer.replaceAll(line -> line.replace("{player}", args[1]).replace("&", "§"));

                        for (String string : notFoundPlayer) {
                            sender.sendMessage(string);
                        }
                        return true;
                    }

                    String forceUnSyncID = syncAPI.getAccount(player);

                    User forceUnSyncUser;
                    try {
                        forceUnSyncUser = DiscordClient.jda.retrieveUserById(forceUnSyncID).complete();
                        if (forceUnSyncUser == null) {
                            ArrayList<String> notFoundDiscord = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.sync-not-found"));
                            notFoundDiscord.replaceAll(line -> line.replace("{discord}", forceUnSyncID).replace("&", "§"));

                            for (String string : notFoundDiscord) {
                                sender.sendMessage(string);
                            }
                            return true;
                        }
                    } catch (Exception ex) {
                        ArrayList<String> notFoundDiscord = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.sync-not-found"));
                        notFoundDiscord.replaceAll(line -> line.replace("{discord}", forceUnSyncID).replace("&", "§"));

                        for (String string : notFoundDiscord) {
                            sender.sendMessage(string);
                        }
                        return true;
                    }

                    syncAPI.unSyncUser(forceUnSyncPlayer);
                    RolesUtils.removeRoles(forceUnSyncPlayer, forceUnSyncUser.getId());
                    RolesUtils.unSetSyncOptions(forceUnSyncUser.getId());

                    ArrayList<String> forceUnSync = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.not-found"));
                    forceUnSync.replaceAll(line -> line.replace("{player}", forceUnSyncPlayer.getName()).replace("{discord}", forceUnSyncUser.getName()).replace("&", "§"));

                    for (String string : forceUnSync) {
                        sender.sendMessage(string);
                    }

                    break;

                default:
                    ArrayList<String> operatorUsage = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.use-operator"));
                    operatorUsage.replaceAll(line -> line.replace("&", "§"));

                    for (String string : operatorUsage) {
                        sender.sendMessage(string);
                    }
            }
        }

        if (args.length == 3) {
            String action = args[0].toLowerCase();

            switch (action) {
                case "forcesync":
                    Player forceSyncPlayer = Bukkit.getPlayer(args[1]);

                    if (forceSyncPlayer == null) {
                        ArrayList<String> notFoundPlayer = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.not-found"));
                        notFoundPlayer.replaceAll(line -> line.replace("{player}", args[1]).replace("&", "§"));

                        for (String string : notFoundPlayer) {
                            sender.sendMessage(string);
                        }
                        return true;
                    }

                    String forceSyncUserID = args[2];

                    User forceSyncID;
                    try {
                        forceSyncID = DiscordClient.jda.retrieveUserById(forceSyncUserID).complete();
                        if (forceSyncID == null) {
                            ArrayList<String> notFoundDiscord = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.sync-not-found"));
                            notFoundDiscord.replaceAll(line -> line.replace("{discord}", args[2]).replace("&", "§"));

                            for (String string : notFoundDiscord) {
                                sender.sendMessage(string);
                            }
                            return true;
                        }
                    } catch (Exception ex) {
                        ArrayList<String> notFoundDiscord = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.sync-not-found"));
                        notFoundDiscord.replaceAll(line -> line.replace("{discord}", args[2]).replace("&", "§"));

                        for (String string : notFoundDiscord) {
                            sender.sendMessage(string);
                        }
                        return true;
                    }

                    syncAPI.syncUser(forceSyncPlayer, forceSyncID.getId());
                    RolesUtils.addRoles(forceSyncPlayer, forceSyncID.getId());
                    RolesUtils.setSyncOptions(forceSyncPlayer, forceSyncID.getId());

                    ArrayList<String> forceSync = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.force-sync"));
                    forceSync.replaceAll(line -> line.replace("{player}", forceSyncPlayer.getName()).replace("{discord}", forceSyncID.getName()).replace("&", "§"));

                    for (String string : forceSync) {
                        sender.sendMessage(string);
                    }

                    break;

                default:
                    ArrayList<String> operatorUsage = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.use-operator"));
                    operatorUsage.replaceAll(line -> line.replace("&", "§"));

                    for (String string : operatorUsage) {
                        sender.sendMessage(string);
                    }
            }
        }

        return true;
    }
}
