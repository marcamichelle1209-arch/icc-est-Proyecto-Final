package structures.implementations;

import structures.graphs.Edge;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
        List<Edge<T>> aristas = new ArrayList<>();

        boolean encontrado = explorar(graph, nodoInicio, nodoFin, nodosVisitados, visitados, rutaActual, aristas);

        if (!encontrado) {
            return new PathResult<>(visitados, new LinkedHashSet<>(), aristas);
        }

        return new PathResult<>(visitados, rutaActual, aristas);
    }

    private boolean explorar(Graph<T> graph, Node<T> actual, Node<T> destino,
                              Set<Node<T>> nodosVisitados, Set<T> visitados,
                              LinkedHashSet<T> rutaActual, List<Edge<T>> aristas) {

        nodosVisitados.add(actual);
        visitados.add(actual.getData());
        rutaActual.add(actual.getData());

        if (actual.equals(destino)) {
            return true;
        }

        for (Node<T> vecino : graph.getGraph().get(actual)) {
            if (!nodosVisitados.contains(vecino)) {
                aristas.add(new Edge<>(actual.getData(), vecino.getData()));
                boolean encontrado = explorar(graph, vecino, destino, nodosVisitados, visitados, rutaActual, aristas);
                if (encontrado) {
                    return true;
                }
            }
        }

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