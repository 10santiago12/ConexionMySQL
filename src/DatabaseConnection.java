import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306?useSSL=false";
    private static final String USER = "root";
    private static String PASSWORD = ""; // Se inicia vacía y se actualizará desde el login

    // Método para actualizar la contraseña a usar
    public static void setPassword(String password) {
        System.out.println(password);
        PASSWORD = password;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
