package controllers;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import models.MapPoint;
import models.VisualizationMode;
import persistence.GraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import structures.graphs.implementations.DFSPathFinder;
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

    // --- Activar modo agregar nodo ---
    public void activarAgregarNodo() {
        agregarNodo = true;
        cancelarSeleccion();
        JOptionPane.showMessageDialog(null, "Modo activado: haz clic en el mapa para ubicar un nodo.");
    }

    // --- Clic sobre el mapa (llamado desde MapPanel) ---
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
            if (Math.abs(p.getX() - x) <= 10 && Math.abs(p.getY() - y) <= 10) {
                return p;
            }
        }
        return null;
    }

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

    // --- Botón "Agregar nodo (último clic)" ---
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

    // --- Botón "Marcar como inicio" ---
    public void marcarComoInicio() {
        if (seleccionPrincipal == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un nodo primero.");
            return;
        }
        nodoInicio = seleccionPrincipal;
        mapPanel.marcarInicio(nodoInicio);
    }

    // --- Botón "Marcar como destino" ---
    public void marcarComoDestino() {
        if (seleccionPrincipal == null) {
            JOptionPane.showMessageDialog(null, "Selecciona un nodo primero.");
            return;
        }
        nodoDestino = seleccionPrincipal;
        mapPanel.marcarDestino(nodoDestino);
    }

    // --- Botón "Conectar seleccionados" ---
    public void conectarSeleccionados() {
        if (seleccionPrincipal == null || seleccionSecundaria == null) {
            JOptionPane.showMessageDialog(null, "Selecciona dos nodos (clic en cada uno).");
            return;
        }
        mapGraph.addEdge(seleccionPrincipal, seleccionSecundaria);
        repository.save(mapGraph);
        mapPanel.repaint();
    }

    // --- Botón "Eliminar nodo seleccionado" ---
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

    // --- Botón "Eliminar conexión seleccionada" ---
    public void eliminarConexionSeleccionada() {
        if (seleccionPrincipal == null || seleccionSecundaria == null) {
            JOptionPane.showMessageDialog(null, "Selecciona los dos nodos de la conexión.");
            return;
        }
        mapGraph.removeEdge(seleccionPrincipal, seleccionSecundaria);
        repository.save(mapGraph);
        mapPanel.repaint();
    }

    // --- Botón "Play / Ejecutar" ---
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

        if (modo == VisualizationMode.EXPLORATION) {
            animarExploracion(resultado);
        } else {
            mapPanel.mostrarRutaFinal(resultado.getPath());
        }

        if (onResultUpdated != null) {
            onResultUpdated.onResult(
                    algoritmo, tiempoMs, resultado.getVisitados().size(),
                    resultado.getVisitados().toString(), resultado.getPath().toString()
            );
        }
    }

    private void animarExploracion(PathResult<MapPoint> resultado) {
        var visitados = new ArrayList<>(resultado.getVisitados());
        Timer timer = new Timer(300, null);
        int[] i = {0};
        timer.addActionListener(e -> {
            if (i[0] < visitados.size()) {
                mapPanel.agregarNodoVisitado(visitados.get(i[0]));
                i[0]++;
            } else {
                timer.stop();
                mapPanel.mostrarRutaFinal(resultado.getPath());
            }
        });
        timer.start();
    }
}