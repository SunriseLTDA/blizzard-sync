package me.lucwsh.blizzardsync.database;

import me.lucwsh.blizzardsync.Main;
import me.lucwsh.blizzardsync.managers.UserManager;

import java.sql.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class DatabaseHandler {

    private final Main main;
    private final DatabaseManager database;

    public DatabaseHandler(Main main, DatabaseManager database) {
        this.main = main;
        this.database = database;
    }

    public void initializeDatabase() {
        try (Connection conn = database.getConnection();
             Statement stat = conn.createStatement()) {
            String sql_user = "CREATE TABLE IF NOT EXISTS sync_user (" +
                    "name VARCHAR(36) PRIMARY KEY, " +
                    "synced TINYINT(1) DEFAULT 0, " +
                    "account VARCHAR(32) DEFAULT NULL, " +
                    "security VARCHAR(10) DEFAULT 'Nenhum' " +
                    ")";
            stat.execute(sql_user);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void executeUpdate(String sql, Consumer<PreparedStatement> setter) throws SQLException {
        try (Connection conn = database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            setter.accept(statement);
            statement.executeUpdate();
        } catch (SQLException ex) {
            main.getLogger().log(Level.SEVERE, "Failed to execute update", ex);
            throw ex;
        }
    }

    public UserManager findUser(String name) {
        String sql = "SELECT * FROM sync_user WHERE name = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UserManager user = new UserManager(resultSet.getString("name"));
                    user.setSynced(resultSet.getBoolean("synced"));
                    user.setAccount(resultSet.getString("account"));
                    user.setSecurity(resultSet.getString("security"));
                    return user;
                }
            }
        } catch (SQLException e) {
            main.getLogger().log(Level.SEVERE, "Failed to find user", e);
        }
        return null;
    }

    public void createUser(UserManager user) throws SQLException {
        String sql = "INSERT INTO sync_user (name, synced, account, security) " +
                "VALUES (?, ?, ?, ?)";
        executeUpdate(sql, statement -> {
            try {
                statement.setString(1, user.getName());
                statement.setBoolean(2, user.getSynced());
                statement.setString(3, user.getAccount());
                statement.setString(4, user.getSecurity());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void deleteUser(UserManager user) throws SQLException {
        String sql = "DELETE FROM sync_user WHERE name = ?";
        executeUpdate(sql, statement -> {
            try {
                statement.setString(1, user.getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void updateUser(UserManager user) throws SQLException {
        String sql = "UPDATE sync_user SET synced = ?, account = ?, security = ? WHERE name = ?";
        executeUpdate(sql, statement -> {
            try {
                statement.setBoolean(1, user.getSynced());
                statement.setString(2, user.getAccount());
                statement.setString(3, user.getSecurity());
                statement.setString(4, user.getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

