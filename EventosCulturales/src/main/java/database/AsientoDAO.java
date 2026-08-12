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
                Asiento asiento = new Asiento(rs.getInt("evento_id"), rs.getString("numero_asiento"), rs.getString("estado"));
                asiento.setId(rs.getInt("id"));
                return asiento;
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


        if (asientos.isEmpty()) {
            generarAsientosFaltantesParaEvento(eventoId);
            asientos = findByEvento(eventoId); //
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

        if (asientos.isEmpty()) {
            List<Asiento> totalAsientos = findByEvento(eventoId);
            if (!totalAsientos.isEmpty()) {
                for (Asiento a : totalAsientos) {
                    if ("disponible".equalsIgnoreCase(a.getEstado())) {
                        asientos.add(a);
                    }
                }
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
                int count = rs.getInt(1);
                if (count == 0) {
                    List<Asiento> disponibles = findDisponiblesByEvento(eventoId);
                    return disponibles.size();
                }
                return count;
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

    private void generarAsientosFaltantesParaEvento(int eventoId) {
        try (Connection conn = dbConnection.getConnection()) {
            String sqlEvento = "SELECT aforo_maximo FROM eventos WHERE id = ?";
            int aforo = 0;
            try (PreparedStatement stmtEv = conn.prepareStatement(sqlEvento)) {
                stmtEv.setInt(1, eventoId);
                try (ResultSet rsEv = stmtEv.executeQuery()) {
                    if (rsEv.next()) {
                        aforo = rsEv.getInt("aforo_maximo");
                    }
                }
            }

            if (aforo > 0) {
                String sqlInsert = "INSERT INTO asientos_aforos (evento_id, numero_asiento, estado) VALUES (?, ?, 'disponible')";

                int asientosPorFila = 20;
                char letraFila = 'A';
                int contadorFila = 0;

                for (int i = 1; i <= aforo; i++) {
                    int numeroAsientoEnFila = contadorFila + 1;
                    String codigoAsiento = String.format("%c%02d", letraFila, numeroAsientoEnFila);

                    try (PreparedStatement stmtIns = conn.prepareStatement(sqlInsert)) {
                        stmtIns.setInt(1, eventoId);
                        stmtIns.setString(2, codigoAsiento);
                        stmtIns.executeUpdate();
                    }

                    contadorFila++;
                    if (contadorFila >= asientosPorFila) {
                        contadorFila = 0;
                        letraFila++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}