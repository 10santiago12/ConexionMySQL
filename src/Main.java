import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Mostrar diálogo de login
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);
            // Solo se abre la ventana principal si la clave es correcta
            if (loginDialog.isSucceeded()) {
                new MainFrame().setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
