package server;

import model.Masina;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:./masiniDB";
    private static final String USER = "sa";
    private static final String PASS = "";

    public DatabaseManager() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS masini (
                vin VARCHAR(20) PRIMARY KEY,
                marca VARCHAR(50),
                model VARCHAR(50),
                an INT
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void adaugaMasina(Masina masina) {
        String sql = "INSERT INTO masini (vin, marca, model, an) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, masina.getVin());
            pstmt.setString(2, masina.getMarca());
            pstmt.setString(3, masina.getModel());
            pstmt.setInt(4, masina.getAnFabricatie());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stergeMasina(String vin) {
        String sql = "DELETE FROM masini WHERE vin = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizeazaMasina(Masina masina) {
        String sql = "UPDATE masini SET marca = ?, model = ?, an = ? WHERE vin = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, masina.getMarca());
            pstmt.setString(2, masina.getModel());
            pstmt.setInt(3, masina.getAnFabricatie());
            pstmt.setString(4, masina.getVin());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Masina> incarcaToateMasinile() {
        Map<String, Masina> masini = new HashMap<>();
        String sql = "SELECT * FROM masini";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String vin = rs.getString("vin");
                String marca = rs.getString("marca");
                String model = rs.getString("model");
                int an = rs.getInt("an");
                masini.put(vin, new Masina(vin, marca, model, an));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return masini;
    }
}