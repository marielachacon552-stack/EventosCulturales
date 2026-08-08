package database;

import data.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private DatabaseConnection dbConnection;

    public ReservaDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void create(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (evento_id, usuario_id, estado, cantidad_boletos, total_pagado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reserva.getEventoId());
            pstmt.setInt(2, reserva.getUsuarioId());
            pstmt.setString(3, reserva.getEstado());
            pstmt.setInt(4, reserva.getCantidadBoletos());
            pstmt.setDouble(5, reserva.getTotalPagado());
            pstmt.executeUpdate();
        }
    }

    public Reserva read(int id) throws SQLException {
        String sql = "SELECT id, evento_id, usuario_id, fecha_reserva, estado, cantidad_boletos, total_pagado FROM reservas WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReserva(rs);
            }
        }
        return null;
    }

    public List<Reserva> readAll() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, evento_id, usuario_id, fecha_reserva, estado, cantidad_boletos, total_pagado FROM reservas ORDER BY fecha_reserva DESC";
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }
        }
        return reservas;
    }

    public void update(Reserva reserva) throws SQLException {
        String sql = "UPDATE reservas SET evento_id = ?, usuario_id = ?, estado = ?, cantidad_boletos = ?, total_pagado = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reserva.getEventoId());
            pstmt.setInt(2, reserva.getUsuarioId());
            pstmt.setString(3, reserva.getEstado());
            pstmt.setInt(4, reserva.getCantidadBoletos());
            pstmt.setDouble(5, reserva.getTotalPagado());
            pstmt.setInt(6, reserva.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Reserva> findByUsuario(int usuarioId) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, evento_id, usuario_id, fecha_reserva, estado, cantidad_boletos, total_pagado FROM reservas WHERE usuario_id = ? ORDER BY fecha_reserva DESC";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }
        }
        return reservas;
    }

    public List<Reserva> findByEvento(int eventoId) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, evento_id, usuario_id, fecha_reserva, estado, cantidad_boletos, total_pagado FROM reservas WHERE evento_id = ? ORDER BY fecha_reserva DESC";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }
        }
        return reservas;
    }

    public int countByEvento(int eventoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservas WHERE evento_id = ? AND estado = 'activa'";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public double getTotalReservasByEvento(int eventoId) throws SQLException {
        String sql = "SELECT SUM(total_pagado) FROM reservas WHERE evento_id = ? AND estado = 'activa'";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, eventoId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    private Reserva mapResultSetToReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(rs.getInt("id"));
        reserva.setEventoId(rs.getInt("evento_id"));
        reserva.setUsuarioId(rs.getInt("usuario_id"));
        if (rs.getTimestamp("fecha_reserva") != null) {
            reserva.setFechaReserva(rs.getTimestamp("fecha_reserva").toLocalDateTime());
        }
        reserva.setEstado(rs.getString("estado"));
        reserva.setCantidadBoletos(rs.getInt("cantidad_boletos"));
        reserva.setTotalPagado(rs.getDouble("total_pagado"));
        return reserva;
    }
}
