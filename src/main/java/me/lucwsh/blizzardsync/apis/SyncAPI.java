package me.lucwsh.blizzardsync.apis;

import me.lucwsh.blizzardsync.cache.UserCache;
import me.lucwsh.blizzardsync.database.DatabaseHandler;
import me.lucwsh.blizzardsync.managers.LoadersManager;
import me.lucwsh.blizzardsync.managers.UserManager;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SyncAPI {

    private static final UserCache userCache = LoadersManager.userCache;
    private static final DatabaseHandler databaseHandler = LoadersManager.databaseHandler;

    public boolean isUserSynced(Player player) {
        String name = player.getName();
        UserManager user = userCache.getOrCreate(name);
        return user.getSynced();
    }

    public void syncUser(Player player, String account) {
        String name = player.getName();
        UserManager user = userCache.getOrCreate(name);
        user.setSynced(true);
        user.setAccount(account);
        try {
            databaseHandler.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unSyncUser(Player player) {
        String name = player.getName();
        UserManager user = userCache.getOrCreate(name);
        user.setSynced(false);
        user.setAccount("Nenhuma");
        try {
            databaseHandler.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSecurity(Player player, String security) {
        String name = player.getName();
        UserManager user = userCache.getOrCreate(name);
        user.setSecurity(security);
        try {
            databaseHandler.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetSecurity(Player player) {
        String name = player.getName();
        UserManager user = userCache.getOrCreate(name);
        user.setSecurity("Nenhum");
        try {
            databaseHandler.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getSecurity(Player player) {
        String name = player.getName();
        UserManager user = userCache.getOrCreate(name);

        if (user.getSecurity() != null) {
            return user.getSecurity();
        } else {
            return "Nenhum";
        }
    }

    public String getAccount(Player player) {
        String name = player.getName();
        UserManager user = userCache.getOrCreate(name);

        if (user.getAccount() != null) {
            return user.getAccount();
        } else {
            return "Nenhuma";
        }
    }
}
