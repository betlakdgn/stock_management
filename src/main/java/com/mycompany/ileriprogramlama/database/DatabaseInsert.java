package com.mycompany.ileriprogramlama.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInsert {
    public static void logIslem(String urunAdi, int miktar, String islemTipi) {
        String sql = "INSERT INTO urun_log (Urun_Adi, Miktar, Islem_Tipi) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, urunAdi);
            stmt.setInt(2, miktar);
            stmt.setString(3, islemTipi);
            stmt.executeUpdate();  // Veriyi ekler
            System.out.println("İşlem başarıyla loglandı.");
        } catch (SQLException e) {
            System.out.println("Veri eklenirken hata oluştu: " + e.getMessage());
        }
    }
    
}
