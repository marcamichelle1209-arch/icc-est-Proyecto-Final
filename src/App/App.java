package App;

import javax.swing.SwingUtilities;


public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PrototipoMenu menu = new PrototipoMenu();
            menu.setVisible(true);
        });
    }
}