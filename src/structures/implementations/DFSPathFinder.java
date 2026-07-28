package structures.implementations;

import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

import java.util.LinkedHashSet;
import java.util.Set;

public class DFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        Node<T> nodoInicio = buscarNodo(graph, start);
        Node<T> nodoFin = buscarNodo(graph, end);

        Set<T> visitados = new LinkedHashSet<>();

        if (nodoInicio == null || nodoFin == null) {
            return PathResult.sinRuta(visitados);
        }

        Set<Node<T>> nodosVisitados = new LinkedHashSet<>();
        LinkedHashSet<T> rutaActual = new LinkedHashSet<>();

        boolean encontrado = explorar(graph, nodoInicio, nodoFin, nodosVisitados, visitados, rutaActual);

        if (!encontrado) {
            return PathResult.sinRuta(visitados);
        }

        return new PathResult<>(visitados, rutaActual);
    }

    // Recursivo: explora en profundidad, aplica retroceso si la rama no lleva al destino
    private boolean explorar(Graph<T> graph, Node<T> actual, Node<T> destino,
                              Set<Node<T>> nodosVisitados, Set<T> visitados, LinkedHashSet<T> rutaActual) {

        nodosVisitados.add(actual);
        visitados.add(actual.getData());
        rutaActual.add(actual.getData());

        if (actual.equals(destino)) {
            return true; // ruta encontrada
        }

        for (Node<T> vecino : graph.getGraph().get(actual)) {
            if (!nodosVisitados.contains(vecino)) {
                boolean encontrado = explorar(graph, vecino, destino, nodosVisitados, visitados, rutaActual);
                if (encontrado) {
                    return true; // propaga el éxito hacia arriba sin seguir buscando
                }
            }
        }

        // Esta rama no llevó al destino: retroceso (quita el nodo actual de la ruta)
        rutaActual.remove(actual.getData());
        return false;
    }

    private Node<T> buscarNodo(Graph<T> graph, T data) {
        for (Node<T> nodo : graph.getNodes()) {
            if (nodo.getData().equals(data)) {
                return nodo;
            }
        }
        return null;
    }
}
