package structures.graphs;

import java.util.List;

public class TopologicalResult<T> {

    private final boolean valido;
    private final List<T> orden;

    public TopologicalResult(boolean valido, List<T> orden) {
        this.valido = valido;
        this.orden = orden;
    }

    // false si el grafo tiene ciclos (no admite orden topologico)
    public boolean isValido() {
        return valido;
    }

    public List<T> getOrden() {
        return orden;
    }
}