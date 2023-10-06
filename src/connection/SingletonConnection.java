package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    private static Connection conn;
    static {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:/minisas", "root", "alaa1234");
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/easybank_etendu", "postgres", "alaa1234");
            System.out.println("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Vous pouvez également imprimer un message d'erreur personnalisé ici
        }
    }

    public static Connection getConn() {
        return conn;
    }
}
