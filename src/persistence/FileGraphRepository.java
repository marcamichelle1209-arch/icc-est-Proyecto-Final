package persistence;

import models.MapPoint;
import structures.graphs.Graph;
import structures.node.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileGraphRepository implements GraphRepository {

    private final String filePath;

    public FileGraphRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Graph<MapPoint> load() {
        Graph<MapPoint> graph = new Graph<>();
        File archivo = new File(filePath);

        List<String> lineas;

        if (archivo.exists()) {
            // Caso normal: existe el archivo externo en disco
            try {
                lineas = Files.readAllLines(Path.of(filePath));
            } catch (IOException e) {
                System.err.println("Error al leer la configuración: " + e.getMessage());
                return graph;
            }
        } else {
            // Respaldo: no existe en disco, buscamos el que viene empaquetado dentro del jar
            try (InputStream input = getClass().getResourceAsStream("/resources/config/mapa.json")) {
                if (input == null) {
                    return graph; // ni externo ni interno: no hay configuración disponible
                }
                lineas = new BufferedReader(new InputStreamReader(input)).lines().toList();
            } catch (IOException e) {
                System.err.println("Error al leer la configuración empaquetada: " + e.getMessage());
                return graph;
            }
        }

        
            String seccion = "";

            for (String linea : lineas) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                if (linea.equalsIgnoreCase("[NODOS]")) {
                    seccion = "NODOS";
                    continue;
                }
                if (linea.equalsIgnoreCase("[ARISTAS]")) {
                    seccion = "ARISTAS";
                    continue;
                }

                if (seccion.equals("NODOS")) {
                    // formato: id,x,y
                    String[] partes = linea.split(",");
                    if (partes.length != 3) {
                        System.err.println("Linea de nodo invalida, se ignora: " + linea);
                        continue;
                    }
                    String id = partes[0].trim();
                    if (id.isEmpty()) {
                        System.err.println("Nodo sin id, se ignora: " + linea);
                        continue;
                    }
                    try {
                        int x = Integer.parseInt(partes[1].trim());
                        int y = Integer.parseInt(partes[2].trim());
                        graph.add(new MapPoint(id, x, y));
                    } catch (NumberFormatException ex) {
                        System.err.println("Coordenadas invalidas, se ignora: " + linea);
                    }
                }

                if (seccion.equals("ARISTAS")) {
                    // formato: idOrigen,idDestino,bidireccional(true/false)
                    String[] partes = linea.split(",");
                    if (partes.length != 3) {
                        System.err.println("Linea de arista invalida, se ignora: " + linea);
                        continue;
                    }
                    String idOrigen = partes[0].trim();
                    String idDestino = partes[1].trim();
                    boolean bidireccional = Boolean.parseBoolean(partes[2].trim());

                    MapPoint origen = buscarPorId(graph, idOrigen);
                    MapPoint destino = buscarPorId(graph, idDestino);

                    if (origen != null && destino != null) {
                        if (bidireccional) {
                            graph.addEdge(origen, destino);
                        } else {
                            graph.addEdgeUni(origen, destino);
                        }
                    } else {
                        System.err.println("Arista referencia nodo inexistente, se ignora: " + linea);
                    }
                }
            
        } 

        return graph;
    }

    @Override
    public void save(Graph<MapPoint> graph) {
        try {
            File archivo = new File(filePath);
            File carpeta = archivo.getParentFile();
            if (carpeta != null) carpeta.mkdirs(); // crea la carpeta config/ si no existe

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write("[NODOS]");
                writer.newLine();
                for (Node<MapPoint> nodo : graph.getNodes()) {
                    MapPoint p = nodo.getData();
                    writer.write(p.getId() + "," + p.getX() + "," + p.getY());
                    writer.newLine();
                }

                writer.newLine();
                writer.write("[ARISTAS]");
                writer.newLine();

                // Para no duplicar aristas bidireccionales al guardar, controlamos pares ya escritos
                List<String> yaEscritas = new ArrayList<>();

                for (Node<MapPoint> nodo : graph.getNodes()) {
                    MapPoint origen = nodo.getData();
                    for (Node<MapPoint> vecino : graph.getGraph().get(nodo)) {
                        MapPoint destino = vecino.getData();

                        boolean esBidireccional = graph.getGraph().get(vecino).contains(nodo);
                        String clave = origen.getId() + "->" + destino.getId();
                        String claveInversa = destino.getId() + "->" + origen.getId();

                        if (esBidireccional) {
                            if (!yaEscritas.contains(clave) && !yaEscritas.contains(claveInversa)) {
                                writer.write(origen.getId() + "," + destino.getId() + ",true");
                                writer.newLine();
                                yaEscritas.add(clave);
                            }
                        } else {
                            writer.write(origen.getId() + "," + destino.getId() + ",false");
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la configuración: " + e.getMessage());
        }
    }

    private MapPoint buscarPorId(Graph<MapPoint> graph, String id) {
        for (Node<MapPoint> nodo : graph.getNodes()) {
            if (nodo.getData().getId().equals(id)) {
                return nodo.getData();
            }
        }
        return null;
    }
}