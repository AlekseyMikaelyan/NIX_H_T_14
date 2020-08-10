package org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {
    private static final String url = "jdbc:postgresql://localhost:5432/dijkstra";
    private static final String userName = "postgres";
    private static final String password = "postgres";
    Connection connection;

    public DataBaseConnector() {
        try {
            connection= DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
