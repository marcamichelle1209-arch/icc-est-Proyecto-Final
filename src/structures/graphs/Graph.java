package structures.graphs;

import structures.node.Node;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

    // Cada nodo apunta a su conjunto de nodos vecinos (conexiones)
    private final Map<Node<T>, Set<Node<T>>> graph = new LinkedHashMap<>();

    // --- Agregar nodo ---
    public void add(T data) {
        Node<T> nodo = new Node<>(data);
        if (!graph.containsKey(nodo)) {
            graph.put(nodo, new LinkedHashSet<>());
        }
    }

    // --- Verificar si existe un nodo ---
    public boolean contains(T data) {
        return graph.containsKey(new Node<>(data));
    }

    // --- Agregar arista bidireccional (calle en ambos sentidos) ---
    public void addEdge(T v1, T v2) {
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);

        add(v1);
        add(v2);

        graph.get(getExistingNode(n1)).add(getExistingNode(n2));
        graph.get(getExistingNode(n2)).add(getExistingNode(n1));
    }

    // --- Agregar arista unidireccional (calle de un solo sentido) ---
    public void addEdgeUni(T v1, T v2) {
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);

        add(v1);
        add(v2);

        graph.get(getExistingNode(n1)).add(getExistingNode(n2));
    }

    // --- Eliminar nodo y todas sus conexiones ---
    public void remove(T data) {
        Node<T> nodo = new Node<>(data);
        graph.remove(nodo);

        // Quitarlo también de las listas de vecinos de los demás nodos
        for (Set<Node<T>> vecinos : graph.values()) {
            vecinos.remove(nodo);
        }
    }

    // --- Eliminar arista bidireccional ---
    public void removeEdge(T v1, T v2) {
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);

        if (graph.containsKey(n1)) graph.get(n1).remove(n2);
        if (graph.containsKey(n2)) graph.get(n2).remove(n1);
    }

    // --- Eliminar arista unidireccional ---
    public void removeEdgeUni(T v1, T v2) {
        Node<T> n1 = new Node<>(v1);
        Node<T> n2 = new Node<>(v2);

        if (graph.containsKey(n1)) graph.get(n1).remove(n2);
    }

    // --- Consultas para dibujar en la interfaz (no modifican la estructura) ---
    public Set<Node<T>> getNodes() {
        return graph.keySet();
    }

    public Map<Node<T>, Set<Node<T>>> getGraph() {
        return graph;
    }

    // --- Método auxiliar interno: obtiene la instancia real de Node ya guardada ---
    private Node<T> getExistingNode(Node<T> nodo) {
        for (Node<T> n : graph.keySet()) {
            if (n.equals(nodo)) return n;
        }
        return nodo;
    }
}