package com.mycompany.ileriprogramlama.database;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS urun_log ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "Urun_Adi TEXT NOT NULL, "
                + "Miktar INTEGER NOT NULL, "
                + "Islem_Tipi TEXT, "
                + "Zaman DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);  // Tabloyu oluşturma
            System.out.println("Tablo başarıyla oluşturuldu.");
        } catch (SQLException e) {
            System.out.println("Tablo oluşturulamadı: " + e.getMessage());
        }
    }
    
}
