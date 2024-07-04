package me.lucwsh.blizzardsync;

import lombok.SneakyThrows;
import me.lucwsh.blizzardsync.database.DatabaseManager;
import me.lucwsh.blizzardsync.discord.DiscordClient;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.managers.LoadersManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    @SneakyThrows
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
        DatabaseManager.disconnect();
        DiscordClient.shutdown();
        Bukkit.getConsoleSender().sendMessage("§a[Sync] §fPlugin disabled!");
    }
}
