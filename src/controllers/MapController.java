package controllers;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import models.MapPoint;
import models.VisualizationMode;
import persistence.GraphRepository;
import structures.graphs.Edge;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.implementations.BFSPathFinder;
import structures.implementations.DFSPathFinder;
import structures.node.Node;
import views.MapPanel;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class MapController {

    private final Graph<MapPoint> mapGraph;
    private final MapPanel mapPanel;
    private final GraphRepository repository;

    private boolean agregarNodo = false;
    private MapPoint ultimoClic = null;
    private MapPoint seleccionPrincipal = null;
    private MapPoint seleccionSecundaria = null;
    private MapPoint nodoInicio = null;
    private MapPoint nodoDestino = null;

    private ResultCallback onResultUpdated;
    private BiConsumer<String, String> onSelectionUpdated;

    public interface ResultCallback {
        void onResult(String algoritmo, long tiempoMs, int visitados, String orden, String ruta);
    }

    public MapController(Graph<MapPoint> mapGraph, MapPanel mapPanel, GraphRepository repository) {
        this.mapGraph = mapGraph;
        this.mapPanel = mapPanel;
        this.repository = repository;
    }

    public void setOnResultUpdated(ResultCallback callback) {
        this.onResultUpdated = callback;
    }

    public void setOnSelectionUpdated(BiConsumer<String, String> callback) {
        this.onSelectionUpdated = callback;
    }

    // --- Siembra automatica: red tipo grid de calles (filas x columnas), como cuadras urbanas ---
    // Cada nodo se conecta con su vecino de la derecha y con el de abajo, simulando cuadras.
    // Solo corre si el grafo esta vacio (no pisa datos guardados previamente).
    public void sembrarNodosSiVacio() {
        if (!mapGraph.getNodes().isEmpty()) {
            return;
        }

        int filas = 4;
        int columnas = 5;
        int inicioX = 480;
        int inicioY = 260;
        int pasoX = 200;
        int pasoY = 170;

        MapPoint[][] grilla = new MapPoint[filas][columnas];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                String id = "" + (char) ('A' + f) + (c + 1); // A1, A2, ... B1, B2, ...
                int x = inicioX + c * pasoX;
                int y = inicioY + f * pasoY;
                MapPoint punto = new MapPoint(id, x, y);
                grilla[f][c] = punto;
                mapGraph.add(punto);
            }
        }

        // conecta cada nodo con su vecino horizontal (misma calle) y vertical (misma avenida)
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (c + 1 < columnas) {
                    mapGraph.addEdge(grilla[f][c], grilla[f][c + 1]);
                }
                if (f + 1 < filas) {
                    mapGraph.addEdge(grilla[f][c], grilla[f + 1][c]);
                }
            }
        }

        repository.save(mapGraph);
        mapPanel.repaint();
    }

    public void activarAgregarNodo() {
        agregarNodo = true;
        cancelarSeleccion();
        JOptionPane.showMessageDialog(null, "Modo activado: haz clic en el mapa para ubicar un nodo.");
    }

    public void onMapClick(int x, int y) {
        ultimoClic = new MapPoint("temp", x, y);

        MapPoint nodoExistente = buscarNodoCercano(x, y);
        if (nodoExistente != null) {
            seleccionarNodo(nodoExistente);
        }
        mapPanel.repaint();
    }

    private MapPoint buscarNodoCercano(int x, int y) {
        for (Node<MapPoint> nodo : mapGraph.getNodes()) {
            MapPoint p = nodo.getData();
            if (Math.abs(p.getX() - x) <= 12 && Math.abs(p.getY() - y) <= 12) {
                return p;
            }
        }
        return null;
    }

    // Al seleccionar el segundo nodo, se dibuja al instante una linea punteada de "vista previa"
    // entre ambos, para que se vea la union antes de confirmar con "Conectar seleccionados".
    private void seleccionarNodo(MapPoint punto) {
        if (seleccionPrincipal == null || seleccionPrincipal.equals(punto)) {
            seleccionPrincipal = punto;
            seleccionSecundaria = null;
        } else {
            seleccionSecundaria = punto;
        }
        mapPanel.marcarSeleccionado(seleccionPrincipal, seleccionSecundaria);
        notificarSeleccion();
    }

    private void notificarSeleccion() {
        if (onSelectionUpdated != null) {
            onSelectionUpdated.accept(
                    seleccionPrincipal != null ? seleccionPrincipal.getId() : "--",
                    seleccionSecundaria != null ? seleccionSecundaria.getId() : "--"
            );
        }
    }

    public void cancelarSeleccion() {
        seleccionPrincipal = null;
        seleccionSecundaria = null;
        mapPanel.limpiarSeleccion();
    }

    public void agregarNodoUltimoClic() {
        if (ultimoClic == null) {
            JOptionPane.showMessageDialog(null, "Haz clic en el mapa primero.");
            return;
        }
        String id = JOptionPane.showInputDialog("ID del nuevo nodo:");
        if (id == null || id.isBlank()) return;

        MapPoint nuevo = new MapPoint(id, ultimoClic.getX(), ultimoClic.getY());
        if (mapGraph.contains(nuevo)) {
            JOptionPane.showMessageDialog(null, "Ya existe un nodo con ese ID.");
            return;
        }
        mapGraph.add(nuevo);
        repository.save(mapGraph);
        mapPanel.repaint();
    }

    public void marcarComoInicio() {
        if (seleccionPrincipal == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un nodo primero.");
            return;
        }
        nodoInicio = seleccionPrincipal;
        mapPanel.marcarInicio(nodoInicio);
    }

    public void marcarComoDestino() {
        if (seleccionPrincipal == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un nodo primero.");
            return;
        }
        nodoDestino = seleccionPrincipal;
        mapPanel.marcarDestino(nodoDestino);
    }

    public void conectarSeleccionados() {
        if (seleccionPrincipal == null || seleccionSecundaria == null) {
            JOptionPane.showMessageDialog(null, "Selecciona dos nodos (clic en cada uno).");
            return;
        }
        mapGraph.addEdge(seleccionPrincipal, seleccionSecundaria);
        repository.save(mapGraph);
        mapPanel.repaint();

        if (nodoInicio != null && nodoDestino != null) {
            JOptionPane.showMessageDialog(null,
                    "Conexion creada. Ya tenes inicio y destino marcados: presiona 'Play / Ejecutar'.");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Conexion creada entre " + seleccionPrincipal.getId() + " y " + seleccionSecundaria.getId() + ".");
        }
    }

    public void eliminarNodoSeleccionado() {
        if (seleccionPrincipal == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un nodo primero.");
            return;
        }
        mapGraph.remove(seleccionPrincipal);
        repository.save(mapGraph);
        cancelarSeleccion();
        mapPanel.repaint();
    }

    public void eliminarConexionSeleccionada() {
        if (seleccionPrincipal == null || seleccionSecundaria == null) {
            JOptionPane.showMessageDialog(null, "Selecciona los dos nodos de la conexión.");
            return;
        }
        mapGraph.removeEdge(seleccionPrincipal, seleccionSecundaria);
        repository.save(mapGraph);
        mapPanel.repaint();
    }

    public void ejecutarBusquedaConSeleccionActual(String algoritmo, VisualizationMode modo) {
        if (nodoInicio == null || nodoDestino == null) {
            JOptionPane.showMessageDialog(null, "Marca un nodo de inicio y uno de destino primero.");
            return;
        }

        PathFinder<MapPoint> finder = algoritmo.equals("BFS")
                ? new BFSPathFinder<>()
                : new DFSPathFinder<>();

        long t0 = System.nanoTime();
        PathResult<MapPoint> resultado = finder.find(mapGraph, nodoInicio, nodoDestino);
        long tiempoMs = (System.nanoTime() - t0) / 1_000_000;

        mapPanel.limpiarRecorrido();

        if (resultado.getPath().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontro un camino entre los nodos seleccionados.");
        } else if (modo == VisualizationMode.EXPLORATION) {
            animarExploracion(resultado);
        } else {
            animarSoloRuta(resultado);
        }

        if (onResultUpdated != null) {
            onResultUpdated.onResult(
                    algoritmo, tiempoMs, resultado.getVisitados().size(),
                    resultado.getVisitados().toString(), resultado.getPath().toString()
            );
        }
    }

    // dibuja cada arista de exploracion una por una: se van uniendo nodo a nodo en tiempo real
    private void animarExploracion(PathResult<MapPoint> resultado) {
        var aristas = new ArrayList<>(resultado.getAristasExploracion());
        Timer timer = new Timer(300, null);
        int[] i = {0};
        timer.addActionListener(e -> {
            if (i[0] < aristas.size()) {
                Edge<MapPoint> arista = aristas.get(i[0]);
                mapPanel.agregarAristaVisitada(arista.getOrigen(), arista.getDestino());
                i[0]++;
            } else {
                timer.stop();
                mapPanel.mostrarRutaFinal(resultado.getPath());
            }
        });
        timer.start();
    }

    // modo FINAL_PATH: la ruta ganadora se va uniendo punto por punto, uno por uno
    private void animarSoloRuta(PathResult<MapPoint> resultado) {
        var puntos = new ArrayList<>(resultado.getPath());
        Timer timer = new Timer(300, null);
        int[] i = {0};
        timer.addActionListener(e -> {
            if (i[0] < puntos.size() - 1) {
                mapPanel.agregarAristaRuta(puntos.get(i[0]), puntos.get(i[0] + 1));
                i[0]++;
            } else {
                timer.stop();
            }
        });
        timer.start();
    }
}