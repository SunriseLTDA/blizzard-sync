package me.lucwsh.blizzardsync.listeners;

import me.lucwsh.blizzardsync.apis.SyncAPI;
import me.lucwsh.blizzardsync.managers.FilesManager;
import me.lucwsh.blizzardsync.managers.LoadersManager;
import me.lucwsh.blizzardsync.managers.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {

    @EventHandler
    public void updateUserOnJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        UserManager user = LoadersManager.userCache.getOrCreate(playerName);
        if (user.getSynced() == null) {
            user.setSynced(false);
        } else if (user.getSynced() == null) {
            user.setAccount("Nenhuma");
        }
    }

    @EventHandler
    public void updateUserOnQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        UserManager user = LoadersManager.userCache.getOrCreate(playerName);
        if (user.getSecurity() != null) {
            user.setSecurity("Nenhum");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SyncAPI api = new SyncAPI();
        Player player = event.getPlayer();
        Boolean synced = api.isUserSynced(player);

        UserManager user = LoadersManager.userCache.getOrCreate(player.getName());

        if (!synced || user.getSynced() == null) {
            ArrayList<String> array = new ArrayList<>(FilesManager.messages.getStringList("messages.sync.not-synced"));
            array.replaceAll(line-> line.replace("&", "§"));

            for (String string : array) {
                player.sendMessage(string);
            }
        }
    }
}
