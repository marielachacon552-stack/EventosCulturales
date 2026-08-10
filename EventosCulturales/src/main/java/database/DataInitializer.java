package database;

import java.sql.*;

public class DataInitializer {
    
    public static void initializeData() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection conn = db.getConnection();
        
        try {
            // Verificar si ya existen datos
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM roles");
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Base de datos ya inicializada");
                return;
            }
            
            // Insertar Roles
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO roles (nombre) VALUES (?)"
            );
            pstmt.setString(1, "Administrador");
            pstmt.executeUpdate();
            pstmt.setString(1, "Organizador");
            pstmt.executeUpdate();
            pstmt.setString(1, "Cliente");
            pstmt.executeUpdate();
            pstmt.close();
            
            // Insertar Usuarios de prueba (contraseñas hasheadas con BCrypt)
            pstmt = conn.prepareStatement(
                "INSERT INTO usuarios (nombre, correo, contrasena_hash, roleId) VALUES (?, ?, ?, ?)"
            );
            
            // Admin
            pstmt.setString(1, "Admin Sistema");
            pstmt.setString(2, "admin@eventos.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // admin123
            pstmt.setInt(4, 1);
            pstmt.executeUpdate();
            
            // Organizador
            pstmt.setString(1, "Juan Organizador");
            pstmt.setString(2, "organizador@eventos.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // admin123
            pstmt.setInt(4, 2);
            pstmt.executeUpdate();
            
            // Clientes
            pstmt.setString(1, "Carlos Cliente");
            pstmt.setString(2, "carlos@cliente.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // admin123
            pstmt.setInt(4, 3);
            pstmt.executeUpdate();
            
            pstmt.setString(1, "María Cliente");
            pstmt.setString(2, "maria@cliente.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // admin123
            pstmt.setInt(4, 3);
            pstmt.executeUpdate();
            
            pstmt.close();
            
            // Insertar Eventos de prueba
            pstmt = conn.prepareStatement(
                "INSERT INTO eventos (titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            pstmt.setString(1, "Romeo y Julieta - Teatro Clásico");
            pstmt.setString(2, "teatro");
            pstmt.setTimestamp(3, Timestamp.valueOf("2026-08-20 19:00:00"));
            pstmt.setInt(4, 120);
            pstmt.setString(5, "Una historia de amor y tragedia en el escenario más hermoso");
            pstmt.setDouble(6, 25.00);
            pstmt.setInt(7, 150);
            pstmt.setString(8, "Teatro Nacional - Centro");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Concierto de Música Clásica");
            pstmt.setString(2, "concierto");
            pstmt.setTimestamp(3, Timestamp.valueOf("2026-08-25 20:00:00"));
            pstmt.setInt(4, 150);
            pstmt.setString(5, "Orquesta Sinfónica en vivo con los mejores músicos del país");
            pstmt.setDouble(6, 35.00);
            pstmt.setInt(7, 200);
            pstmt.setString(8, "Centro de Convenciones");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Exposición de Arte Moderno");
            pstmt.setString(2, "exposición");
            pstmt.setTimestamp(3, Timestamp.valueOf("2026-09-01 10:00:00"));
            pstmt.setInt(4, 480);
            pstmt.setString(5, "Obras de artistas contemporáneos latinoamericanos");
            pstmt.setDouble(6, 10.00);
            pstmt.setInt(7, 300);
            pstmt.setString(8, "Museo de Arte Contemporáneo");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Concierto de Jazz - La Banda Azul");
            pstmt.setString(2, "concierto");
            pstmt.setTimestamp(3, Timestamp.valueOf("2026-09-05 21:00:00"));
            pstmt.setInt(4, 120);
            pstmt.setString(5, "Noche de jazz puro con la famosa banda azul");
            pstmt.setDouble(6, 30.00);
            pstmt.setInt(7, 180);
            pstmt.setString(8, "Club de Jazz Moderno");
            pstmt.executeUpdate();
            
            pstmt.setString(1, "Hamletl - Adaptación Moderna");
            pstmt.setString(2, "teatro");
            pstmt.setTimestamp(3, Timestamp.valueOf("2026-09-10 18:30:00"));
            pstmt.setInt(4, 180);
            pstmt.setString(5, "Versión moderna del clásico de Shakespeare");
            pstmt.setDouble(6, 28.00);
            pstmt.setInt(7, 160);
            pstmt.setString(8, "Teatro Experimental");
            pstmt.executeUpdate();
            
            pstmt.close();
            
            // Crear asientos para cada evento
            pstmt = conn.prepareStatement(
                "INSERT INTO asientos_aforos (evento_id, numero_asiento, estado) VALUES (?, ?, 'disponible')"
            );
            
            for (int evento = 1; evento <= 5; evento++) {
                ResultSet rsEvento = stmt.executeQuery("SELECT aforo_maximo FROM eventos WHERE id = " + evento);
                rsEvento.next();
                int aforoMax = rsEvento.getInt(1);
                
                // Crear asientos (A1, A2, ... B1, B2, etc)
                int contador = 1;
                for (char fila = 'A'; fila < 'Z' && contador <= aforoMax; fila++) {
                    for (int num = 1; num <= 20 && contador <= aforoMax; num++) {
                        pstmt.setInt(1, evento);
                        pstmt.setString(2, fila + String.valueOf(num));
                        pstmt.executeUpdate();
                        contador++;
                    }
                }
            }
            pstmt.close();
            
            System.out.println("✓ Base de datos inicializada correctamente");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
