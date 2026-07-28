package structures.implementations;

import structures.graphs.CycleResult;
import structures.graphs.Graph;
import structures.graphs.TopologicalResult;
import structures.node.Node;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

// Ordenamiento topologico (Kahn) usando CycleDetector para invalidar si hay ciclos
public class TopologicalSorter<T> {

    public TopologicalResult<T> sort(Graph<T> graph) {
        CycleDetector<T> detector = new CycleDetector<>();
        CycleResult<T> ciclo = detector.detect(graph);
        if (ciclo.hasCycle()) {
            return new TopologicalResult<>(false, new ArrayList<>());
        }

        Map<Node<T>, Integer> gradoEntrada = new HashMap<>();
        for (Node<T> nodo : graph.getNodes()) {
            gradoEntrada.put(nodo, 0);
        }
        for (Node<T> nodo : graph.getNodes()) {
            for (Node<T> vecino : graph.getGraph().get(nodo)) {
                gradoEntrada.merge(vecino, 1, Integer::sum);
            }
        }

        Queue<Node<T>> cola = new ArrayDeque<>();
        for (Map.Entry<Node<T>, Integer> entry : gradoEntrada.entrySet()) {
            if (entry.getValue() == 0) cola.add(entry.getKey());
        }

        List<T> orden = new ArrayList<>();
        while (!cola.isEmpty()) {
            Node<T> actual = cola.poll();
            orden.add(actual.getData());
            for (Node<T> vecino : graph.getGraph().get(actual)) {
                int nuevoGrado = gradoEntrada.get(vecino) - 1;
                gradoEntrada.put(vecino, nuevoGrado);
                if (nuevoGrado == 0) cola.add(vecino);
            }
        }

        return new TopologicalResult<>(true, orden);
    }
}