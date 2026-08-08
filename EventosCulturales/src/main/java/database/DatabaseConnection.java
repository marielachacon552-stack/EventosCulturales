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

            // Tabla Usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "correo TEXT NOT NULL UNIQUE," +
                    "contrasena_hash TEXT NOT NULL," +
                    "roleId INTEGER NOT NULL," +
                    "fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (roleId) REFERENCES roles(id)" +
                    ")");

            // Tabla Eventos
            stmt.execute("CREATE TABLE IF NOT EXISTS eventos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo TEXT NOT NULL," +
                    "categoria TEXT NOT NULL," +
                    "fecha_hora TIMESTAMP NOT NULL," +
                    "duracion INTEGER NOT NULL," +
                    "descripcion TEXT," +
                    "precio_boleto REAL NOT NULL," +
                    "aforo_maximo INTEGER NOT NULL," +
                    "ubicacion TEXT NOT NULL," +
                    "estado TEXT DEFAULT 'activo'," +
                    "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Tabla Asientos_Aforos
            stmt.execute("CREATE TABLE IF NOT EXISTS asientos_aforos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "evento_id INTEGER NOT NULL," +
                    "numero_asiento TEXT NOT NULL," +
                    "estado TEXT DEFAULT 'disponible'," +
                    "FOREIGN KEY (evento_id) REFERENCES eventos(id)," +
                    "UNIQUE(evento_id, numero_asiento)" +
                    ")");

            // Tabla Boletos
            stmt.execute("CREATE TABLE IF NOT EXISTS boletos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "evento_id INTEGER NOT NULL," +
                    "usuario_id INTEGER NOT NULL," +
                    "numero_asiento TEXT NOT NULL," +
                    "estado TEXT DEFAULT 'activo'," +
                    "fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "codigo_boleto TEXT UNIQUE," +
                    "FOREIGN KEY (evento_id) REFERENCES eventos(id)," +
                    "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                    ")");

            // Tabla Reservas
            stmt.execute("CREATE TABLE IF NOT EXISTS reservas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "evento_id INTEGER NOT NULL," +
                    "usuario_id INTEGER NOT NULL," +
                    "fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "estado TEXT DEFAULT 'activa'," +
                    "cantidad_boletos INTEGER NOT NULL," +
                    "total_pagado REAL NOT NULL," +
                    "FOREIGN KEY (evento_id) REFERENCES eventos(id)," +
                    "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                    ")");

            // Crear índices para mejor rendimiento
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_usuarios_correo ON usuarios(correo)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_eventos_categoria ON eventos(categoria)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_boletos_usuario ON boletos(usuario_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_boletos_evento ON boletos(evento_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_reservas_usuario ON reservas(usuario_id)");

            // --- INSERCIÓN AUTOMÁTICA DE DATOS INICIALES ---
            stmt.execute("INSERT OR IGNORE INTO roles (id, nombre) VALUES (1, 'Administrador')");
            stmt.execute("INSERT OR IGNORE INTO roles (id, nombre) VALUES (2, 'Organizador')");
            stmt.execute("INSERT OR IGNORE INTO roles (id, nombre) VALUES (3, 'Cliente')");

            stmt.execute("INSERT OR IGNORE INTO usuarios (id, nombre, correo, contrasena_hash, roleId) " +
                    "VALUES (1, 'Admin Sistema', 'admin@eventos.hn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1)");

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