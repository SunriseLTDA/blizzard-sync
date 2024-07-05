package me.lucwsh.blizzardsync.tasks;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.cache.UserCache;
import me.lucwsh.blizzardsync.database.DatabaseHandler;
import me.lucwsh.blizzardsync.managers.UserManager;

import java.sql.SQLException;
import java.util.Map;

public class AutoSaveTask implements Runnable{
    private final UserCache userCache;
    private final DatabaseHandler databaseHandler;

    public AutoSaveTask(Main main, UserCache userCache, DatabaseHandler databaseHandler) {
        this.userCache = userCache;
        this.databaseHandler = databaseHandler;

        main.getServer().getScheduler().runTaskTimerAsynchronously(main, this, 0L, 60L);
    }


    @Override
    public void run() {
        for (Map.Entry<String, UserManager> entry : userCache.userMap.entrySet()) {
            UserManager user = entry.getValue();
            try {
                databaseHandler.updateUser(user);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
