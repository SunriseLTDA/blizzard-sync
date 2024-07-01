package me.lucwsh.blizzardsync.managers;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.commands.SyncCommand;
import me.lucwsh.blizzardsync.inventories.SyncInventory;
import me.lucwsh.blizzardsync.listeners.InventoryListener;
import org.bukkit.Bukkit;

public class LoadersManager {
    public static void registerCommands() {
        try {
            Main.instance.getCommand("sync").setExecutor(new SyncCommand());

            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fIn total, [1] commands were loaded successfully");

        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fAn error occurred while loading the commands!");
        }
    }

    public static void registerListeners() {
        try {

            Bukkit.getPluginManager().registerEvents(new InventoryListener(), Main.instance);
            Bukkit.getPluginManager().registerEvents(new SyncInventory(), Main.instance);

            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fIn total, [2] listeners were loaded successfully");

        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fAn error occurred while loading the listeners!");
        }
    }

    public static void registerConfig() {
        try {
            Main.instance.getConfig().options().copyDefaults(false);
            Main.instance.saveDefaultConfig();

            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fGeneral configuration loaded successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fAn error occurred while loading the general configuration!");
        }
    }
}
