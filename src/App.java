import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.MouseInputAdapter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class App {
    public static void main(String[] args) throws Exception {
        
        //creacion del frame 
        JFrame frmiVentana = new JFrame("Proyecto");
        JPanel plnmiPanel = new JPanel();


        // se le esta dando un tañaño a la pantalla
        frmiVentana.add(plnmiPanel);
        frmiVentana.setSize(600,600);
       

        //creacion del menu flotante
        JPopupMenu menuFlotante = new JPopupMenu();

        //elementos que estaran en el menu

        JMenuItem itemBorrarNodo = new JMenuItem("Borrar Nodo");
        JMenuItem itemAnadirNodo =new JMenuItem("Añadir Nodo");

        menuFlotante.add( itemAnadirNodo);
        menuFlotante.addSeparator();
         menuFlotante.add(itemBorrarNodo);

        itemBorrarNodo.addActionListener(e -> JOptionPane.showMessageDialog(frmiVentana,""));
         itemAnadirNodo.addActionListener(e -> JOptionPane.showMessageDialog(frmiVentana, "Se ha añadido otro nodo"));
        

         //esto es para mostrar el menu cuando detecte el click derecho
         plnmiPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                if(e.isPopupTrigger()){
                    menuFlotante.show(e.getComponent(),e.getX(), e.getY());
                }
            }
            @Override

            public void mouseReleased(MouseEvent e){
                if (e.isPopupTrigger()) {
                    menuFlotante.show(e.getComponent(),e.getX(), e.getY());
                }
            }
         });
       

           
        frmiVentana.setVisible(true);
    }
}
