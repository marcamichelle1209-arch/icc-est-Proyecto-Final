package App;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import views.MainFrame;

public class PrototipoMenu extends JFrame {

    public PrototipoMenu() {
        setTitle("Menú Principal - Mapa de Rutas");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton btnIniciar = new JButton("Iniciar Mapa");
        btnIniciar.addActionListener(e -> abrirMapa());

        JPanel panel = new JPanel();
        panel.add(btnIniciar);
        add(panel);
    }

    private void abrirMapa() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        this.dispose(); // cierra el menú al abrir el mapa
    }
}