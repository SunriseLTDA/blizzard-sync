package me.lucwsh.blizzardsync.managers;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.cache.UserCache;
import me.lucwsh.blizzardsync.commands.DiscordCommand;
import me.lucwsh.blizzardsync.commands.SyncCommand;
import me.lucwsh.blizzardsync.database.DatabaseHandler;
import me.lucwsh.blizzardsync.database.DatabaseManager;
import me.lucwsh.blizzardsync.discord.DiscordClient;
import me.lucwsh.blizzardsync.inventories.SyncInventory;
import me.lucwsh.blizzardsync.listeners.InventoryListener;
import me.lucwsh.blizzardsync.listeners.PlayerListener;
import me.lucwsh.blizzardsync.tasks.AutoSaveTask;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class LoadersManager {

    public static UserCache userCache;
    public static DatabaseManager database;
    public static DatabaseHandler databaseHandler;

    public static void registerDatabase() {
        database = new DatabaseManager(Main.instance);
        try {
            database.connect();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        databaseHandler = new DatabaseHandler(Main.instance, database);
        userCache = new UserCache(databaseHandler);

        databaseHandler.initializeDatabase();
    }

    public static void registerCommands() {
        try {
            Main.instance.getCommand("sync").setExecutor(new SyncCommand());
            Main.instance.getCommand("discord").setExecutor(new DiscordCommand());

            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fIn total, [2] commands were loaded successfully");

        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fAn error occurred while loading the commands!");
        }
    }

    public static void registerListeners() {
        try {
            Bukkit.getPluginManager().registerEvents(new InventoryListener(), Main.instance);
            Bukkit.getPluginManager().registerEvents(new SyncInventory(), Main.instance);
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), Main.instance);

            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fIn total, [3] listeners were loaded successfully");

        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fAn error occurred while loading the listeners!");
        }
    }

    public static void registerBot() {
        String token = FilesManager.discord.getString("discord.app-token");

        if (token == null || token.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fInvalid bot token!");
            Main.instance.getServer().getPluginManager().disablePlugin(Main.instance);
        }

        try {
            DiscordClient.initialize(token);
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fBot initialized!");
        } catch (LoginException err) {
            throw new RuntimeException(err);
        }
    }

    public static void registerTasks() {
        new AutoSaveTask(Main.instance, userCache, databaseHandler);
    }

    public static void registerConfig() {
        try {
            Main.instance.getConfig().options().copyDefaults(false);
            Main.instance.saveDefaultConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fAn error occurred while loading the config file!");
        }
    }
}