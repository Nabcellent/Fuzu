package com.Nabangi.fuzu.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class DatabaseLink {
    private static final int PORT = 3306;
    private static final String HOST = "localhost";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "M1gu3l.";
    private static final String DATABASE = "db_michael_nabangi_134694";
    private static Connection connection;

    public static Connection connectDb() {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE), USERNAME, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseLink.class.getName()).log(Level.SEVERE, null, ex);
        }

        return connection;
    }
}
