package com.mycompany.ileriprogramlama.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSelect {
    public static void getAllLogs() {
        String sql = "SELECT * FROM urun_log";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String urunAdi = rs.getString("Urun_Adi");
                int miktar = rs.getInt("Miktar");
                String islemTipi = rs.getString("Islem_Tipi");
                String zaman = rs.getString("Zaman");

                System.out.println("ID: " + id + ", Ürün Adı: " + urunAdi + ", Miktar: " + miktar
                        + ", İşlem Tipi: " + islemTipi + ", Zaman: " + zaman);
            }
        } catch (SQLException e) {
            System.out.println("Veri okunurken hata oluştu: " + e.getMessage());
        }
    }
}
