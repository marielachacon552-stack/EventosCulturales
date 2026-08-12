package database;

import data.Evento;
import data.Asiento;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    private DatabaseConnection dbConnection;

    public EventoDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void create(Evento evento) throws SQLException {
        String sql = "INSERT INTO eventos (titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion, estado, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'activo', datetime('now'))";
        int eventoId = -1;

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, evento.getTitulo());
            pstmt.setString(2, evento.getCategoria());
            pstmt.setString(3, evento.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setInt(4, evento.getDuracion());
            pstmt.setString(5, evento.getDescripcion());
            pstmt.setDouble(6, evento.getPrecioBoleto());
            pstmt.setInt(7, evento.getAforoMaximo());
            pstmt.setString(8, evento.getUbicacion());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    eventoId = generatedKeys.getInt(1);
                }
            }
        }

        if (eventoId != -1 && evento.getAforoMaximo() > 0) {
            generarAsientosParaEvento(eventoId, evento.getAforoMaximo());
        }
    }

    private void generarAsientosParaEvento(int eventoId, int aforo) {
        String sqlAsiento = "INSERT INTO asientos_aforos (evento_id, numero_asiento, estado) VALUES (?, ?, 'disponible')";
        try (Connection conn = dbConnection.getConnection()) {
            int asientosPorFila = 20;
            char letraFila = 'A';
            int contadorFila = 0;

            for (int i = 1; i <= aforo; i++) {
                int numeroAsientoEnFila = contadorFila + 1;
                String codigoAsiento = letraFila + String.valueOf(numeroAsientoEnFila);

                try (PreparedStatement pstmt = conn.prepareStatement(sqlAsiento)) {
                    pstmt.setInt(1, eventoId);
                    pstmt.setString(2, codigoAsiento);
                    pstmt.executeUpdate();
                }

                contadorFila++;
                if (contadorFila >= asientosPorFila) {
                    contadorFila = 0;
                    letraFila++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            pstmt.setString(3, evento.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setInt(4, evento.getDuracion());
            pstmt.setString(5, evento.getDescripcion());
            pstmt.setDouble(6, evento.getPrecioBoleto());
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

        String fechaStr = rs.getString("fecha_hora");
        if (fechaStr != null && !fechaStr.isEmpty()) {
            if (fechaStr.contains("T")) {
                evento.setFechaHora(LocalDateTime.parse(fechaStr));
            } else if (fechaStr.contains(" ")) {
                evento.setFechaHora(LocalDateTime.parse(fechaStr.replace(" ", "T")));
            } else {
                try {
                    long millis = Long.parseLong(fechaStr);
                    evento.setFechaHora(LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(millis), java.time.ZoneId.systemDefault()));
                } catch (NumberFormatException e) {
                    evento.setFechaHora(LocalDateTime.now());
                }
            }
        }

        evento.setDuracion(rs.getInt("duracion"));
        evento.setDescripcion(rs.getString("descripcion"));
        evento.setPrecioBoleto(rs.getDouble("precio_boleto"));
        evento.setAforoMaximo(rs.getInt("aforo_maximo"));
        evento.setUbicacion(rs.getString("ubicacion"));
        evento.setEstado(rs.getString("estado"));

        String fechaCreacionStr = rs.getString("fecha_creacion");
        if (fechaCreacionStr != null && !fechaCreacionStr.isEmpty()) {
            if (fechaCreacionStr.contains("T")) {
                evento.setFechaCreacion(LocalDateTime.parse(fechaCreacionStr));
            } else if (fechaCreacionStr.contains("@@")) {
                evento.setFechaCreacion(LocalDateTime.now());
            } else if (fechaCreacionStr.contains(" ")) {
                evento.setFechaCreacion(LocalDateTime.parse(fechaCreacionStr.replace(" ", "T")));
            }
        }

        return evento;
    }
}