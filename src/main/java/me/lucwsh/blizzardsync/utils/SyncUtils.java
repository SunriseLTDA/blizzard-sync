package me.lucwsh.blizzardsync.utils;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.apis.SyncAPI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.SecureRandom;

public class SyncUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateSecurityCode(Player player, int length) {
        String securityCode = generateRandomCode(length);
        SyncAPI syncAPI = new SyncAPI();
        syncAPI.setSecurity(player, securityCode);

        new BukkitRunnable() {
            @Override
            public void run() {
                syncAPI.resetSecurity(player);
            }
        }.runTaskLaterAsynchronously(Main.instance, 30 * 20L);

        return securityCode;
    }

    private static String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
