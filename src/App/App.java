package App;

import javax.swing.SwingUtilities;
import views.MainFrame;

/**
 * Punto de entrada real del proyecto.
 *
 * Antes, ni App.java ni PrototipoMenu.java abrían la ventana principal
 * (MainFrame): eran solo prototipos aislados de un menú flotante y no
 * formaban parte del flujo real de la aplicación. Esta clase es la que
 * hay que ejecutar para iniciar el programa completo (mapa + BFS/DFS).
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}