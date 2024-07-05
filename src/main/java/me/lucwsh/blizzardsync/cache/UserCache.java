package me.lucwsh.blizzardsync.cache;

import me.lucwsh.blizzardsync.database.DatabaseHandler;
import me.lucwsh.blizzardsync.managers.UserManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserCache {

    private final DatabaseHandler databaseHandler;
    public Map<String, UserManager> userMap = new HashMap<>();

    public UserCache(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public UserManager getUser(String name) {
        return userMap.get(name);
    }

    public void addUser(String name, UserManager user) {
        userMap.put(name, user);
    }

    public UserManager getOrCreate(String name) {
        UserManager user = getUser(name);

        if (user == null) {
            user = databaseHandler.findUser(name);
            if (user == null) {
                user = new UserManager(name);
                try {
                    databaseHandler.createUser(user);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            addUser(name, user);
        }

        return user;
    }

}