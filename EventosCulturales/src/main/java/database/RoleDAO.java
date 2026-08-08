package database;

import data.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private DatabaseConnection dbConnection;

    public RoleDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public void create(Role role) throws SQLException {
        String sql = "INSERT INTO roles (nombre) VALUES (?)";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, role.getNombre());
            pstmt.executeUpdate();
        }
    }

    public Role read(int id) throws SQLException {
        String sql = "SELECT id, nombre FROM roles WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("id"), rs.getString("nombre"));
            }
        }
        return null;
    }

    public List<Role> readAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, nombre FROM roles";
        try (Statement stmt = dbConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                roles.add(new Role(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return roles;
    }

    public void update(Role role) throws SQLException {
        String sql = "UPDATE roles SET nombre = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, role.getNombre());
            pstmt.setInt(2, role.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM roles WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Role findByNombre(String nombre) throws SQLException {
        String sql = "SELECT id, nombre FROM roles WHERE nombre = ?";
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("id"), rs.getString("nombre"));
            }
        }
        return null;
    }
}
