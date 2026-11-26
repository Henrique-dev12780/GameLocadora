
package br.com.gamelocadora.app;

import br.com.gamelocadora.view.TelaPrincipal;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new TelaPrincipal().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Falha ao iniciar",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
