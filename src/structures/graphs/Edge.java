package structures.graphs;

// Representa una arista recorrida durante la exploracion (origen -> destino)
public class Edge<T> {
    private final T origen;
    private final T destino;

    public Edge(T origen, T destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public T getOrigen() {
        return origen;
    }

    public T getDestino() {
        return destino;
    }
}