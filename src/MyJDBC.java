import java.sql.*;

public class MyJDBC {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/world", "root", "12345678"
            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT Name, Population FROM country WHERE Continent = 'South America'"
            );

            while (resultSet.next()) {
                System.out.println("Country: " + resultSet.getString("Name") +
                        ", Population: " + resultSet.getInt("Population"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}