package com.mycompany.ileriprogramlama.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:depo_db.sqlite"; // SQLite dosya yolu


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
}
