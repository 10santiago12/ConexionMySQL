import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private DatabaseDAO dao;

    private JComboBox<String> databaseComboBox;  // Para seleccionar base de datos
    private JComboBox<String> tableComboBox;     // Para seleccionar tabla
    private JButton structureButton;             // Botón para mostrar estructura
    private JButton dataButton;                  // Botón para mostrar datos (registros)

    private JTable resultTable;                  // Tabla para desplegar la información
    private DefaultTableModel tableModel;

    public MainFrame() {
        super("MySQL Database Browser");
        dao = new DatabaseDAO();

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        loadDatabases(); // Cargamos las bases de datos al iniciar
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel superior (combos y botones)
        JPanel topPanel = new JPanel(new FlowLayout());

        // Combo de bases de datos
        databaseComboBox = new JComboBox<>();
        // Cuando el usuario seleccione una base de datos, cargamos sus tablas
        databaseComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selectedDb = (String) databaseComboBox.getSelectedItem();
                loadTables(selectedDb);
            }
        });
        topPanel.add(new JLabel("Databases:"));
        topPanel.add(databaseComboBox);

        // Combo de tablas
        tableComboBox = new JComboBox<>();
        topPanel.add(new JLabel("Tables:"));
        topPanel.add(tableComboBox);

        // Botón para mostrar estructura
        structureButton = new JButton("Mostrar Estructura");
        structureButton.addActionListener(e -> showTableStructure());
        topPanel.add(structureButton);

        // Botón para mostrar datos
        dataButton = new JButton("Mostrar Datos");
        dataButton.addActionListener(e -> showTableData());
        topPanel.add(dataButton);

        // Agregamos el panel superior al frame
        add(topPanel, BorderLayout.NORTH);

        // Panel central con la JTable
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Cargar la lista de bases de datos en el combo
    private void loadDatabases() {
        List<String> databases = dao.getDatabases();
        databaseComboBox.removeAllItems();
        for (String db : databases) {
            databaseComboBox.addItem(db);
        }
    }

    // Cargar las tablas de la base de datos seleccionada
    private void loadTables(String databaseName) {
        if (databaseName == null) return;
        List<String> tables = dao.getTables(databaseName);
        tableComboBox.removeAllItems();
        for (String tbl : tables) {
            tableComboBox.addItem(tbl);
        }
    }

    // Mostrar la estructura de la tabla seleccionada
    private void showTableStructure() {
        String selectedDb = (String) databaseComboBox.getSelectedItem();
        String selectedTbl = (String) tableComboBox.getSelectedItem();
        if (selectedDb == null || selectedTbl == null) return;

        // Limpiamos la JTable
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Definimos las columnas fijas para DESCRIBE
        tableModel.addColumn("Field");
        tableModel.addColumn("Type");
        tableModel.addColumn("Null");
        tableModel.addColumn("Key");
        tableModel.addColumn("Default");
        tableModel.addColumn("Extra");

        // Obtenemos la estructura y la agregamos a la JTable
        List<String[]> structure = dao.getTableStructure(selectedDb, selectedTbl);
        for (String[] row : structure) {
            tableModel.addRow(row);
        }
    }

    // Mostrar todos los registros de la tabla seleccionada
    private void showTableData() {
        String selectedDb = (String) databaseComboBox.getSelectedItem();
        String selectedTbl = (String) tableComboBox.getSelectedItem();
        if (selectedDb == null || selectedTbl == null) return;

        // Limpiamos la JTable
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Primero obtenemos las columnas para el encabezado
        List<String> columns = dao.getTableColumns(selectedDb, selectedTbl);
        for (String col : columns) {
            tableModel.addColumn(col);
        }

        // Luego obtenemos los datos
        List<List<Object>> data = dao.getTableData(selectedDb, selectedTbl);
        for (List<Object> row : data) {
            tableModel.addRow(row.toArray());
        }
    }
}
