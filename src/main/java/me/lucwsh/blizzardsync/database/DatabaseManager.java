package me.lucwsh.blizzardsync.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.lucwsh.blizzardsync.Main;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private final Main main;
    private static HikariDataSource dataSource;

    public DatabaseManager(Main main) {
        this.main = main;
        setupDataSource();
    }

    private void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + main.getConfig().getString("mysql.host") + ":" +
                main.getConfig().getString("mysql.port") + "/" +
                main.getConfig().getString("mysql.database") + "?useSSL=false");
        config.setUsername(main.getConfig().getString("mysql.username"));
        config.setPassword(main.getConfig().getString("mysql.password"));
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void connect() throws SQLException {
        if (dataSource == null || dataSource.getConnection().isClosed()) {
            setupDataSource();
        }
    }

    public static void disconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
