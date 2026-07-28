package structures.implementations;

import structures.graphs.CycleResult;
import structures.graphs.Graph;
import structures.node.Node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// Detecta ciclos en un grafo dirigido usando DFS con pila de recursion
public class CycleDetector<T> {

    public CycleResult<T> detect(Graph<T> graph) {
        Set<Node<T>> visitados = new LinkedHashSet<>();
        Set<Node<T>> enPila = new LinkedHashSet<>();
        List<T> ciclo = new ArrayList<>();

        for (Node<T> nodo : graph.getNodes()) {
            if (!visitados.contains(nodo)) {
                if (explorar(graph, nodo, visitados, enPila, ciclo)) {
                    return new CycleResult<>(true, ciclo);
                }
            }
        }

        return new CycleResult<>(false, ciclo);
    }

    private boolean explorar(Graph<T> graph, Node<T> actual, Set<Node<T>> visitados,
                              Set<Node<T>> enPila, List<T> ciclo) {

        visitados.add(actual);
        enPila.add(actual);
        ciclo.add(actual.getData());

        for (Node<T> vecino : graph.getGraph().get(actual)) {
            if (enPila.contains(vecino)) {
                ciclo.add(vecino.getData());
                return true; // arista hacia un nodo ya en la pila de recursion => ciclo
            }
            if (!visitados.contains(vecino)) {
                if (explorar(graph, vecino, visitados, enPila, ciclo)) {
                    return true;
                }
            }
        }

        enPila.remove(actual);
        ciclo.remove(ciclo.size() - 1);
        return false;
    }
}