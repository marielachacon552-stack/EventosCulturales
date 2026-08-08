package database;

import data.Usuario;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final DatabaseConnection dbConnection;

    public UsuarioDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void create(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena_hash, roleId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setInt(4, usuario.getRoleId());
            pstmt.executeUpdate();
        }
    }

    public Usuario read(int id) throws SQLException {
        String sql = "SELECT id, nombre, correo, contrasena_hash, roleId, fecha_registro FROM usuarios WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena_hash"),
                        rs.getInt("roleId")
                );
                if (rs.getTimestamp("fecha_registro") != null) {
                    usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                }
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> readAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, contrasena_hash, roleId, fecha_registro FROM usuarios";
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena_hash"),
                        rs.getInt("roleId")
                );
                if (rs.getTimestamp("fecha_registro") != null) {
                    usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                }
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, contrasena_hash = ?, roleId = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setInt(4, usuario.getRoleId());
            pstmt.setInt(5, usuario.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Usuario findByCorreo(String correo) throws SQLException {
        String sql = "SELECT id, nombre, correo, contrasena_hash, roleId, fecha_registro FROM usuarios WHERE correo = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena_hash"),
                        rs.getInt("roleId")
                );
                if (rs.getTimestamp("fecha_registro") != null) {
                    usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                }
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> findByRoleId(int roleId) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, contrasena_hash, roleId, fecha_registro FROM usuarios WHERE roleId = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, roleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena_hash"),
                        rs.getInt("roleId")
                );
                if (rs.getTimestamp("fecha_registro") != null) {
                    usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                }
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
}