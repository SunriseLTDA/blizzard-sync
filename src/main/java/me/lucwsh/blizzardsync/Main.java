package me.lucwsh.blizzardsync;

import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.database.DatabaseManager;
import me.lucwsh.blizzardsync.discord.DiscordClient;
import me.lucwsh.blizzardsync.inventories.SyncInventory;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.managers.LoadersManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage("§a\n" +
                "§a   _______     ___   _  _____ \n" +
                "§a  / ____\\ \\   / | \\ | |/ ____|\n" +
                "§a | (___  \\ \\_/ /|  \\| | |     \n" +
                "§a  \\___ \\  \\   / | . ` | |     \n" +
                "§a  ____) |  | |  | |\\  | |____ \n" +
                "§a |_____/   |_|  |_| \\_|\\_____|\n");


        FilesManager.registerFiles();
        LoadersManager.registerConfig();
        LoadersManager.registerDatabase();
        LoadersManager.registerTasks();
        LoadersManager.registerCommands();
        LoadersManager.registerListeners();
        LoadersManager.registerBot();

        Bukkit.getConsoleSender().sendMessage("§a[Sync] §fPlugin started!");

    }

    @Override
    public void onDisable() {

        SyncAPI syncAPI = new SyncAPI();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (SyncInventory.openMenus.containsKey(player)) {
                player.getOpenInventory().close();
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (syncAPI.getSecurity(player) != null) {
                syncAPI.setSecurity(player, "Nenhum");
            }
        }

        try {
            DiscordClient.shutdown();
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§a[Sync] §fBot did not shut down correctly!");
            ex.printStackTrace();
        }

        DatabaseManager.disconnect();

        Bukkit.getConsoleSender().sendMessage("§a[Sync] §fPlugin disabled!");
    }
}
