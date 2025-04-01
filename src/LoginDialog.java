import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginDialog extends JDialog {
    private boolean succeeded;
    private JPasswordField passwordField;
    private String enteredPassword; // Almacena la clave ingresada

    public LoginDialog(Frame parent) {
        super(parent, "Acceso al Sistema", true);
        setLayout(new BorderLayout(10, 10));

        // Título principal en la parte superior
        JLabel titleLabel = new JLabel("Ingrese la contraseña de MySQL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Panel central para la etiqueta y el campo de contraseña
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Margen alrededor de cada componente
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta "Contraseña"
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Contraseña: "), gbc);

        // Campo de contraseña
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        centerPanel.add(passwordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String key = new String(passwordField.getPassword());
                try {
                    // Se asigna temporalmente la clave al DatabaseConnection
                    DatabaseConnection.setPassword(key);
                    Connection conn = DatabaseConnection.getConnection();
                    conn.close();

                    // Si la conexión es exitosa, guardamos la contraseña y cerramos el diálogo
                    enteredPassword = key;
                    succeeded = true;
                    dispose();
                } catch (SQLException ex) {
                    // Si ocurre un error (clave incorrecta o problema de conexión), se informa al usuario
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Error de autenticación:\nClave incorrecta o problema al conectar a MySQL.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                    succeeded = false;
                }
            }
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                succeeded = false;
                dispose();
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getEnteredPassword() {
        return enteredPassword;
    }
}
