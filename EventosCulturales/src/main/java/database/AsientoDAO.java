package database;

import data.Asiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsientoDAO {
    private DatabaseConnection dbConnection;

    public AsientoDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void create(Asiento asiento) throws SQLException {
        String sql = "INSERT INTO asientos_aforos (evento_id, numero_asiento, estado) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, asiento.getEventoId());
            pstmt.setString(2, asiento.getNumeroAsiento());
            pstmt.setString(3, asiento.getEstado());
            pstmt.executeUpdate();
        }
    }

    public Asiento read(int id) throws SQLException {
        String sql = "SELECT id, evento_id, numero_asiento, estado FROM asientos_aforos WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Asiento(rs.getInt("evento_id"), rs.getString("numero_asiento"), rs.getString("estado"));
            }
        }
        return null;
    }

    public List<Asiento> readAll() throws SQLException {
        List<Asiento> asientos = new ArrayList<>();
        String sql = "SELECT id, evento_id, numero_asiento, estado FROM asientos_aforos";
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Asiento asiento = new Asiento(rs.getInt("evento_id"), rs.getString("numero_asiento"), rs.getString("estado"));
                asiento.setId(rs.getInt("id"));
                asientos.add(asiento);
            }
        }
        return asientos;
    }

    public void update(Asiento asiento) throws SQLException {
        String sql = "UPDATE asientos_aforos SET evento_id = ?, numero_asiento = ?, estado = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, asiento.getEventoId());
            pstmt.setString(2, asiento.getNumeroAsiento());
            pstmt.setString(3, asiento.getEstado());
            pstmt.setInt(4, asiento.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM asientos_aforos WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Asiento> findByEvento(int eventoId) throws SQLException {
        List<Asiento> asientos = new ArrayList<>();
        String sql = "SELECT id, evento_id, numero_asiento, estado FROM asientos_aforos WHERE evento_id = ? ORDER BY numero_asiento";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Asiento asiento = new Asiento(rs.getInt("evento_id"), rs.getString("numero_asiento"), rs.getString("estado"));
                asiento.setId(rs.getInt("id"));
                asientos.add(asiento);
            }
        }
        return asientos;
    }

    public List<Asiento> findDisponiblesByEvento(int eventoId) throws SQLException {
        List<Asiento> asientos = new ArrayList<>();
        String sql = "SELECT id, evento_id, numero_asiento, estado FROM asientos_aforos WHERE evento_id = ? AND estado = 'disponible' ORDER BY numero_asiento";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Asiento asiento = new Asiento(rs.getInt("evento_id"), rs.getString("numero_asiento"), rs.getString("estado"));
                asiento.setId(rs.getInt("id"));
                asientos.add(asiento);
            }
        }
        return asientos;
    }

    public int countDisponiblesByEvento(int eventoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM asientos_aforos WHERE evento_id = ? AND estado = 'disponible'";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public void updateEstado(int eventoId, String numeroAsiento, String nuevoEstado) throws SQLException {
        String sql = "UPDATE asientos_aforos SET estado = ? WHERE evento_id = ? AND numero_asiento = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, eventoId);
            pstmt.setString(3, numeroAsiento);
            pstmt.executeUpdate();
        }
    }
}
