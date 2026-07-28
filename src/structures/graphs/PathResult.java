package structures.graphs;

import java.util.LinkedHashSet;
import java.util.Set;

public class PathResult<T> {

    private final Set<T> visitados;
    private final Set<T> path;

    public PathResult(Set<T> visitados, Set<T> path) {
        this.visitados = visitados;
        this.path = path;
    }

    public Set<T> getVisitados() {
        return visitados;
    }

    public Set<T> getPath() {
        return path;
    }

    // Constructor útil para cuando no se encuentra ninguna ruta
    public static <T> PathResult<T> sinRuta(Set<T> visitados) {
        return new PathResult<>(visitados, new LinkedHashSet<>());
    }
}