package database;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("========================================");
        System.out.println("🎭 SISTEMA DE EVENTOS CULTURALES");
        System.out.println("Iniciando aplicación...");
        System.out.println("========================================");
        
        try {
            // Inicializar conexión a la base de datos
            DatabaseConnection db = DatabaseConnection.getInstance();
            System.out.println("✓ Conexión a base de datos establecida");
            
            // Inicializar datos
            DataInitializer.initializeData();
            System.out.println("✓ Base de datos inicializada");
            
            System.out.println("========================================");
            System.out.println("✅ Aplicación iniciada correctamente");
            System.out.println("========================================\n");
            
        } catch (Exception e) {
            System.err.println("❌ Error durante la inicialización:");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("🔴 Aplicación detenida");
        DatabaseConnection.getInstance().closeConnection();
    }
}
