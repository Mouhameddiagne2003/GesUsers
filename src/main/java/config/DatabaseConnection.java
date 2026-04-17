package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer la connexion à la base de données MySQL
 * Utilise le pattern Singleton pour une seule instance de configuration
 */
public class DatabaseConnection {
    
    // Configuration de la base de données
    private static final String URL = getEnvOrDefault("GESUSERS_DB_URL", "jdbc:mysql://localhost:3306/gesusers_db");
    private static final String USER = getEnvOrDefault("GESUSERS_DB_USER", "root");
    private static final String PASSWORD = getEnvOrDefault("GESUSERS_DB_PASSWORD", "root");
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }
    
    // Bloc statique pour charger le driver JDBC
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("✓ Driver MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Erreur : Driver MySQL introuvable !");
            e.printStackTrace();
        }
    }
    
    /**
     * Obtenir une nouvelle connexion à la base de données
     * @return Connection object
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Connexion à la base de données établie");
            return conn;
        } catch (SQLException e) {
            System.err.println("✗ Erreur de connexion à la base de données");
            throw e;
        }
    }
    
    /**
     * Fermer proprement une connexion
     * @param conn Connection à fermer
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✓ Connexion fermée");
            } catch (SQLException e) {
                System.err.println("✗ Erreur lors de la fermeture de la connexion");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Tester la connexion à la base de données
     * @return true si la connexion réussit
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("✗ Test de connexion échoué : " + e.getMessage());
            return false;
        }
    }
}
