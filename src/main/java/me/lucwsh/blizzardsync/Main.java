package me.lucwsh.blizzardsync;

import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.managers.LoadersManager;
import org.bukkit.Bukkit;
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
        LoadersManager.registerCommands();
        LoadersManager.registerListeners();

        Bukkit.getConsoleSender().sendMessage("§a[Sync] §fPlugin started!");

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§a[Sync] §fPlugin disabled!");
    }
}
