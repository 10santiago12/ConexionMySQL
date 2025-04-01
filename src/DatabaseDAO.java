import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDAO {

    // 1. Listar todas las bases de datos
    public List<String> getDatabases() {
        List<String> databases = new ArrayList<>();
        String sql = "SHOW DATABASES";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                databases.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databases;
    }

    // 2. Listar las tablas de una base de datos específica
    public List<String> getTables(String databaseName) {
        List<String> tables = new ArrayList<>();
        String sql = "SHOW TABLES FROM " + databaseName;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    // 3. Obtener la estructura (columnas, tipos, etc.) de una tabla
    public List<String[]> getTableStructure(String databaseName, String tableName) {
        // DESCRIBE <base>.<tabla>
        List<String[]> structure = new ArrayList<>();
        String sql = "DESCRIBE " + databaseName + "." + tableName;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Campos que devuelve DESCRIBE: Field, Type, Null, Key, Default, Extra
                String field = rs.getString("Field");
                String type = rs.getString("Type");
                String isNull = rs.getString("Null");
                String key = rs.getString("Key");
                String defaultValue = rs.getString("Default");
                String extra = rs.getString("Extra");
                structure.add(new String[] { field, type, isNull, key, defaultValue, extra });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structure;
    }

    // 4. Obtener todos los registros de una tabla
    public List<List<Object>> getTableData(String databaseName, String tableName) {
        List<List<Object>> data = new ArrayList<>();
        String sql = "SELECT * FROM " + databaseName + "." + tableName;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Leer metadatos para conocer cuántas columnas hay
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Extraer cada fila
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Método de apoyo para conocer las columnas de la tabla (para construir el encabezado de la JTable)
    public List<String> getTableColumns(String databaseName, String tableName) {
        List<String> columns = new ArrayList<>();
        // Con LIMIT 1 basta para leer la estructura
        String sql = "SELECT * FROM " + databaseName + "." + tableName + " LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }
}
