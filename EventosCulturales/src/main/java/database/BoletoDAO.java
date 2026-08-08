package database;

import data.Boleto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoletoDAO {
    private DatabaseConnection dbConnection;

    public BoletoDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void create(Boleto boleto) throws SQLException {
        String sql = "INSERT INTO boletos (evento_id, usuario_id, numero_asiento, estado, codigo_boleto) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, boleto.getEventoId());
            pstmt.setInt(2, boleto.getUsuarioId());
            pstmt.setString(3, boleto.getNumeroAsiento());
            pstmt.setString(4, boleto.getEstado());
            pstmt.setString(5, boleto.getCodigoBoleto());
            pstmt.executeUpdate();
        }
    }

    public Boleto read(int id) throws SQLException {
        String sql = "SELECT id, evento_id, usuario_id, numero_asiento, estado, fecha_compra, codigo_boleto FROM boletos WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBoleto(rs);
            }
        }
        return null;
    }

    public List<Boleto> readAll() throws SQLException {
        List<Boleto> boletos = new ArrayList<>();
        String sql = "SELECT id, evento_id, usuario_id, numero_asiento, estado, fecha_compra, codigo_boleto FROM boletos ORDER BY fecha_compra DESC";
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                boletos.add(mapResultSetToBoleto(rs));
            }
        }
        return boletos;
    }

    public void update(Boleto boleto) throws SQLException {
        String sql = "UPDATE boletos SET evento_id = ?, usuario_id = ?, numero_asiento = ?, estado = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, boleto.getEventoId());
            pstmt.setInt(2, boleto.getUsuarioId());
            pstmt.setString(3, boleto.getNumeroAsiento());
            pstmt.setString(4, boleto.getEstado());
            pstmt.setInt(5, boleto.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM boletos WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Boleto> findByUsuario(int usuarioId) throws SQLException {
        List<Boleto> boletos = new ArrayList<>();
        String sql = "SELECT id, evento_id, usuario_id, numero_asiento, estado, fecha_compra, codigo_boleto FROM boletos WHERE usuario_id = ? ORDER BY fecha_compra DESC";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boletos.add(mapResultSetToBoleto(rs));
            }
        }
        return boletos;
    }

    public List<Boleto> findByEvento(int eventoId) throws SQLException {
        List<Boleto> boletos = new ArrayList<>();
        String sql = "SELECT id, evento_id, usuario_id, numero_asiento, estado, fecha_compra, codigo_boleto FROM boletos WHERE evento_id = ? ORDER BY fecha_compra DESC";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                boletos.add(mapResultSetToBoleto(rs));
            }
        }
        return boletos;
    }

    public int countByEvento(int eventoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM boletos WHERE evento_id = ? AND estado = 'activo'";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public double getTotalVentasByEvento(int eventoId) throws SQLException {
        String sql = "SELECT SUM(e.precio_boleto) FROM boletos b JOIN eventos e ON b.evento_id = e.id WHERE b.evento_id = ? AND b.estado = 'activo'";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    public Boleto findByCodigoBoleto(String codigo) throws SQLException {
        String sql = "SELECT id, evento_id, usuario_id, numero_asiento, estado, fecha_compra, codigo_boleto FROM boletos WHERE codigo_boleto = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBoleto(rs);
            }
        }
        return null;
    }

    private Boleto mapResultSetToBoleto(ResultSet rs) throws SQLException {
        Boleto boleto = new Boleto();
        boleto.setId(rs.getInt("id"));
        boleto.setEventoId(rs.getInt("evento_id"));
        boleto.setUsuarioId(rs.getInt("usuario_id"));
        boleto.setNumeroAsiento(rs.getString("numero_asiento"));
        boleto.setEstado(rs.getString("estado"));
        if (rs.getTimestamp("fecha_compra") != null) {
            boleto.setFechaCompra(rs.getTimestamp("fecha_compra").toLocalDateTime());
        }
        boleto.setCodigoBoleto(rs.getString("codigo_boleto"));
        return boleto;
    }
}
