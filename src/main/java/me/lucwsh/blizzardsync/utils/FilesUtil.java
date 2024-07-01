package me.lucwsh.blizzardsync.utils;

import me.lucwsh.blizzardsync.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FilesUtil {
    public static YamlConfiguration createFile(Main main, String fileName, Boolean replace) {
        File file = new File(main.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            main.saveResource(fileName, replace);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void saveFile(YamlConfiguration config, String fileName) throws IOException {
        File file = new File(Main.instance.getDataFolder(), fileName);
        config.save(file);
    }
}
