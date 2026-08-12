package database;

import java.sql.*;

public class DataInitializer {
    
    public static void initializeData() {
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection conn = db.getConnection();
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM roles");
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Base de datos ya inicializada");
                return;
            }

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

            pstmt = conn.prepareStatement(
                "INSERT INTO usuarios (nombre, correo, contrasena_hash, roleId) VALUES (?, ?, ?, ?)"
            );

            pstmt.setString(1, "Admin Sistema");
            pstmt.setString(2, "admin@eventos.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // admin123
            pstmt.setInt(4, 1);
            pstmt.executeUpdate();

            pstmt.setString(1, "Organizador");
            pstmt.setString(2, "organizador@eventos.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // org123
            pstmt.setInt(4, 2);
            pstmt.executeUpdate();
            
            // Clientes
            pstmt.setString(1, "Jennifer");
            pstmt.setString(2, "jennifer@cliente.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // 1234
            pstmt.setInt(4, 3);
            pstmt.executeUpdate();

            pstmt.setString(1, "Mariela");
            pstmt.setString(2, "mariela@cliente.hn");
            pstmt.setString(3, "$2a$10$TMrAOtZ7V8esQNynIrwQt.DFp0z7NMOj/4Q7iITKgXl8nYSTwVZiu"); // 1234
            pstmt.setInt(4, 3);
            pstmt.executeUpdate();

            pstmt.close();
            

            pstmt = conn.prepareStatement(
                "INSERT INTO eventos (titulo, categoria, fecha_hora, duracion, descripcion, precio_boleto, aforo_maximo, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );


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
