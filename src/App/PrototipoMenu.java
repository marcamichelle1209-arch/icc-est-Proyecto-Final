package App;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Prototipo aislado del menu flotante, sin depender del resto del proyecto
public class PrototipoMenu {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prototipo Menu Flotante");
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPopupMenu menuFlotante = new JPopupMenu();
        JMenuItem itemAnadir = new JMenuItem("Añadir Nodo");
        JMenuItem itemBorrar = new JMenuItem("Borrar Nodo");
        menuFlotante.add(itemAnadir);
        menuFlotante.addSeparator();
        menuFlotante.add(itemBorrar);

        itemAnadir.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Nodo añadido"));
        itemBorrar.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Nodo borrado"));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) menuFlotante.show(e.getComponent(), e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) menuFlotante.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        frame.setVisible(true);
    }
}