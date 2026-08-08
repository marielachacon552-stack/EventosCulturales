package database;

import data.Evento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    private DatabaseConnection dbConnection;

    public EventoDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void create(Evento evento) throws SQLException {
        String sql = "INSERT INTO eventos (titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, evento.getTitulo());
            pstmt.setString(2, evento.getCategoria());
            pstmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaHora()));
            pstmt.setInt(4, evento.getDuracion());
            pstmt.setString(5, evento.getDescripcion());
            pstmt.setDouble(6, evento.getPrecioBoieto());
            pstmt.setInt(7, evento.getAforoMaximo());
            pstmt.setString(8, evento.getUbicacion());
            pstmt.executeUpdate();
        }
    }

    public Evento read(int id) throws SQLException {
        String sql = "SELECT id, titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion, estado, fecha_creacion FROM eventos WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEvento(rs);
            }
        }
        return null;
    }

    public List<Evento> readAll() throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT id, titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion, estado, fecha_creacion FROM eventos ORDER BY fecha_hora DESC";
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                eventos.add(mapResultSetToEvento(rs));
            }
        }
        return eventos;
    }

    public void update(Evento evento) throws SQLException {
        String sql = "UPDATE eventos SET titulo = ?, categoria = ?, fecha_hora = ?, duracion = ?, descripcion = ?, precio_boleto = ?, aforo_maximo = ?, ubicacion = ?, estado = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, evento.getTitulo());
            pstmt.setString(2, evento.getCategoria());
            pstmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaHora()));
            pstmt.setInt(4, evento.getDuracion());
            pstmt.setString(5, evento.getDescripcion());
            pstmt.setDouble(6, evento.getPrecioBoieto());
            pstmt.setInt(7, evento.getAforoMaximo());
            pstmt.setString(8, evento.getUbicacion());
            pstmt.setString(9, evento.getEstado());
            pstmt.setInt(10, evento.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM eventos WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Evento> findByCategoria(String categoria) throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT id, titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion, estado, fecha_creacion FROM eventos WHERE categoria = ? ORDER BY fecha_hora";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, categoria);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                eventos.add(mapResultSetToEvento(rs));
            }
        }
        return eventos;
    }

    public List<Evento> findProximos() throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT id, titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion, estado, fecha_creacion FROM eventos WHERE fecha_hora > datetime('now') AND estado = 'activo' ORDER BY fecha_hora LIMIT 10";
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                eventos.add(mapResultSetToEvento(rs));
            }
        }
        return eventos;
    }

    private Evento mapResultSetToEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setId(rs.getInt("id"));
        evento.setTitulo(rs.getString("titulo"));
        evento.setCategoria(rs.getString("categoria"));
        evento.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        evento.setDuracion(rs.getInt("duracion"));
        evento.setDescripcion(rs.getString("descripcion"));
        evento.setPrecioBoieto(rs.getDouble("precio_boleto"));
        evento.setAforoMaximo(rs.getInt("aforo_maximo"));
        evento.setUbicacion(rs.getString("ubicacion"));
        evento.setEstado(rs.getString("estado"));
        if (rs.getTimestamp("fecha_creacion") != null) {
            evento.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        }
        return evento;
    }
}
