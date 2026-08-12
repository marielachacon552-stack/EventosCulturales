package database;

import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private static final String DB_PATH = "C:/Jennifer/2026/Segundo Periodo/PW2/EventosCulturales/EventosCulturales/src/main/resources/eventosculturales.db";

    private DatabaseConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            initializeTables();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar a la base de datos", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void initializeTables() {
        try (Statement stmt = connection.createStatement()) {
            // Tabla Roles
            stmt.execute("CREATE TABLE IF NOT EXISTS roles (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL UNIQUE" +
                    ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "correo TEXT NOT NULL UNIQUE," +
                    "contrasena_hash TEXT NOT NULL," +
                    "roleId INTEGER NOT NULL," +
                    "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (roleId) REFERENCES roles(id)" +
                    ")");

            stmt.execute("INSERT OR IGNORE INTO roles (id, nombre) VALUES (1, 'Administrador')");
            stmt.execute("INSERT OR IGNORE INTO roles (id, nombre) VALUES (2, 'Organizador')");
            stmt.execute("INSERT OR IGNORE INTO roles (id, nombre) VALUES (3, 'Cliente')");

            stmt.execute("INSERT OR REPLACE INTO usuarios (id, nombre, correo, contrasena_hash, roleId) " +
                    "VALUES (1, 'Admin Sistema', 'admin@eventos.hn', '$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu', 1)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}