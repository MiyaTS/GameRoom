package com.my.game_room.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseManager {
    private static final Logger LOG = LogManager.getLogger();
    private static Connection connection;

    /**
     * connecting to the database
     * @return connection
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "postgrespw");
            LOG.info("Відкрито конект в БД");
        }
        return connection;
    }

    /**
     * closing the connection to the database
     */
    public static void closeConnection() {
        LOG.info("Закриття конекту з БД");
        try {
            if (connection == null || connection.isClosed()){
                return;
            }
            connection.close();
        } catch (SQLException e) {
            LOG.error("Не вдалось закрити конект до бд");
        }
    }
}
