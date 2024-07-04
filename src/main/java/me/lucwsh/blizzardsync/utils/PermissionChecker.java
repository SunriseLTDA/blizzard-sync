package me.lucwsh.blizzardsync.utils;

import me.lucwsh.blizzardsync.managers.FilesManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PermissionChecker {
    public static boolean hasPermission(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            Location location = player.getLocation();
            Sound sound = Sound.ENTITY_VILLAGER_NO;
            player.playSound(location, sound, 0.5F, 0.5F);
            String actionbarMessage = FilesManager.messages.getString("messages.basic.without-permission");
            actionbarMessage = actionbarMessage.replace("&", "§");
            player.sendActionBar(actionbarMessage);
            return false;
        }
        return true;
    }
}
