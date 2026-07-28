package structures.graphs;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PathResult<T> {

    private final Set<T> visitados;
    private final Set<T> path;
    private final List<Edge<T>> aristasExploracion;

    public PathResult(Set<T> visitados, Set<T> path, List<Edge<T>> aristasExploracion) {
        this.visitados = visitados;
        this.path = path;
        this.aristasExploracion = aristasExploracion;
    }

    public Set<T> getVisitados() {
        return visitados;
    }

    public Set<T> getPath() {
        return path;
    }

    // aristas en el orden exacto en que el algoritmo las recorrio (para animar)
    public List<Edge<T>> getAristasExploracion() {
        return aristasExploracion;
    }

    public static <T> PathResult<T> sinRuta(Set<T> visitados) {
        return new PathResult<>(visitados, new LinkedHashSet<>(), new ArrayList<>());
    }
}