package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.MapController;
import models.MapPoint;
import models.VisualizationMode;
import persistence.FileGraphRepository;
import persistence.GraphRepository;
import structures.graphs.Graph;

public class MainFrame extends JFrame{
    
   
    private JComboBox<String> comboAlgoritmo;
    private JComboBox<String> comboModo;
   private MapPanel mapPanel;
   private MapController controller;
   private JLabel lblResultado;

   public MainFrame(){

    setTitle("Mapa de Calles");
    setSize(800,500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    //
    GraphRepository repository = new FileGraphRepository("config/map.json");
    Graph<MapPoint> mGraph = repository.load();

    //creando el panel del mapa
    mapPanel = new MapPanel("map.png", mGraph);
    controller = new MapController(mGraph,mapPanel,repository);
    mapPanel.setController(controller);

    controller.setOnResultUpdated(this::actualizarPanelResultados);
    controller.setOnSelectionUpdated(this::actulizarSeleccionLabel);

    add(buildSidebar(), BorderLayout.WEST);
    add(mapPanel, BorderLayout.CENTER);
    add(buildResultPanel(), BorderLayout.SOUTH);


   }
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sidebar.add(new JLabel("Algoritmo"));
        comboAlgoritmo = new JComboBox<>(new String[]{"BFS", "DFS"});
        sidebar.add(comboAlgoritmo);
        sidebar.add(Box.createVerticalStrut(15));

        sidebar.add(new JLabel("Modo"));
        comboModo = new JComboBox<>(new String[]{"EXPLORATION", "FINAL_PATH"});
        sidebar.add(comboModo);
        sidebar.add(Box.createVerticalStrut(15));

        sidebar.add(botonAccion("Play / Ejecutar", e -> ejecutarBusqueda()));
        sidebar.add(botonAccion("Limpiar recorrido", e -> mapPanel.limpiarRecorrido()));
        sidebar.add(botonAccion("Marcar como inicio", e -> controller.marcarComoInicio()));
        sidebar.add(botonAccion("Marcar como destino", e -> controller.marcarComoDestino()));
        sidebar.add(botonAccion("Agregar nodo (último clic)", e -> controller.agregarNodoUltimoClic()));
        sidebar.add(botonAccion("Eliminar nodo seleccionado", e -> controller.eliminarNodoSeleccionado()));
        sidebar.add(botonAccion("Conectar seleccionados", e -> controller.conectarSeleccionados()));
        sidebar.add(botonAccion("Eliminar conexión seleccionada", e -> controller.eliminarConexionSeleccionada()));

        return sidebar;
    }
   private JButton botonAccion(String texto, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 30));
        btn.addActionListener(listener);
        return btn;
    }

    private JPanel buildResultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        lblResultado = new JLabel(" ");
        panel.add(lblResultado);
        return panel;
    }

    private void ejecutarBusqueda() {
        String algoritmo = (String) comboAlgoritmo.getSelectedItem();
        VisualizationMode modo = comboModo.getSelectedItem().equals("EXPLORATION")
                ? VisualizationMode.EXPLORATION
                : VisualizationMode.FINAL_PATH;

        controller.ejecutarBusquedaConSeleccionActual(algoritmo, modo);
    }
    private void actualizarPanelResultados(String algoritmo, long tiempoMs, int visitados,
                                            String ordenVisitados, String rutaEncontrada) {
        lblResultado.setText(String.format(
                "<html>Algoritmo: %s | Tiempo: %d ms | Visitados: %d<br>" +
                "Orden visitados: %s<br>Ruta encontrada: %s</html>",
                algoritmo, tiempoMs, visitados, ordenVisitados, rutaEncontrada));
    }

    private void actualizarSeleccionLabel(String principal, String secundaria) {
        lblResultado.setText(lblResultado.getText() +
                String.format(" | Selección actual: %s | secundaria: %s", principal, secundaria));
    }

}
