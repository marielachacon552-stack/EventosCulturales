package database;

import data.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> readAll() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, roleId, fecha_registro FROM usuarios";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setRoleId(rs.getInt("roleId"));

                // Conversión correcta de Timestamp a LocalDateTime
                Timestamp timestamp = rs.getTimestamp("fecha_registro");
                if (timestamp != null) {
                    u.setFechaRegistro(timestamp.toLocalDateTime());
                }

                lista.add(u);
            }
        }
        return lista;
    }

    public boolean update(Usuario usuario) throws SQLException {
        boolean actualizarPass = (usuario.getContrasena() != null && !usuario.getContrasena().trim().isEmpty());
        String sql;

        if (actualizarPass) {
            sql = "UPDATE usuarios SET nombre = ?, correo = ?, contrasena_hash = ?, roleId = ? WHERE id = ?";
        } else {
            sql = "UPDATE usuarios SET nombre = ?, correo = ?, roleId = ? WHERE id = ?";
        }

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());

            if (actualizarPass) {
                pstmt.setString(3, usuario.getContrasena());
                pstmt.setInt(4, usuario.getRoleId());
                pstmt.setInt(5, usuario.getId());
            } else {
                pstmt.setInt(3, usuario.getRoleId());
                pstmt.setInt(4, usuario.getId());
            }

            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Usuario read(int id) throws SQLException {
        String sql = "SELECT id, nombre, correo, roleId, fecha_registro FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setCorreo(rs.getString("correo"));
                    u.setRoleId(rs.getInt("roleId"));

                    Timestamp timestamp = rs.getTimestamp("fecha_registro");
                    if (timestamp != null) {
                        u.setFechaRegistro(timestamp.toLocalDateTime());
                    }

                    return u;
                }
            }
        }
        return null;
    }

    public Usuario findByCorreo(String correo) throws SQLException {
        String sql = "SELECT id, nombre, correo, contrasena_hash, roleId, fecha_registro FROM usuarios WHERE correo = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setCorreo(rs.getString("correo"));
                    u.setContrasena(rs.getString("contrasena_hash"));
                    u.setRoleId(rs.getInt("roleId"));

                    Timestamp timestamp = rs.getTimestamp("fecha_registro");
                    if (timestamp != null) {
                        u.setFechaRegistro(timestamp.toLocalDateTime());
                    }

                    return u;
                }
            }
        }
        return null;
    }

    public boolean create(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena_hash, roleId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setInt(4, usuario.getRoleId());
            return pstmt.executeUpdate() > 0;
        }
    }
}