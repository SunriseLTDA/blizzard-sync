package me.lucwsh.blizzardsync.managers;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.utils.FilesUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class FilesManager {


    public static void registerFiles() {

        try {
            loadMessages();
            loadLinkMenu();
            loadDiscord();
            loadPermissions();

            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fIn total, [4] files were loaded successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fAn error occurred while loading the files!");
        }
    }

    // Messages

    public static YamlConfiguration messages;

    public static void loadMessages() {
        messages = FilesUtil.createFile(Main.instance, "messages/messages.yml", false);
    }

    // Menus

    public static YamlConfiguration syncMenu;

    public static void loadLinkMenu() {
        syncMenu = FilesUtil.createFile(Main.instance, "menus/sync.yml", false);
    }

    // Permissions

    public static YamlConfiguration permissions;

    public static void loadPermissions() {
        permissions = FilesUtil.createFile(Main.instance, "permissions.yml", false);
    }

    // Discord

    public static YamlConfiguration discord;
    public static YamlConfiguration roles;

    public static void loadDiscord() {
        discord = FilesUtil.createFile(Main.instance, "discord.yml", false);
    }

}
