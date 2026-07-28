package structures.implementations;

import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        Node<T> nodoInicio = buscarNodo(graph, start);
        Node<T> nodoFin = buscarNodo(graph, end);

        Set<T> visitados = new LinkedHashSet<>();

        if (nodoInicio == null || nodoFin == null) {
            
            return PathResult.sinRuta(visitados);
        }

        Queue<Node<T>> cola = new LinkedList<>();
        Map<Node<T>, Node<T>> predecesores = new LinkedHashMap<>();
        Set<Node<T>> nodosVisitados = new LinkedHashSet<>();

        cola.add(nodoInicio);
        nodosVisitados.add(nodoInicio);

        boolean encontrado = false;

        while (!cola.isEmpty()) {
            Node<T> actual = cola.poll();
            visitados.add(actual.getData());

            if (actual.equals(nodoFin)) {
                encontrado = true;
                break;
            }

            for (Node<T> vecino : graph.getGraph().get(actual)) {
                if (!nodosVisitados.contains(vecino)) {
                    nodosVisitados.add(vecino);
                    predecesores.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        if (!encontrado) {

            return PathResult.sinRuta(visitados);
        }

        // Reconstruir la ruta desde el destino hasta el inicio usando predecesores
        Set<T> path = new LinkedHashSet<>();
        LinkedList<T> pathInverso = new LinkedList<>();
        Node<T> actual = nodoFin;

        while (actual != null) {
            pathInverso.addFirst(actual.getData());
            actual = predecesores.get(actual);
        }
        path.addAll(pathInverso);

        return new PathResult<>(visitados, path);
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
