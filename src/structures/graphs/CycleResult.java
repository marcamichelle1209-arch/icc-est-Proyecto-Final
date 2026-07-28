package structures.graphs;

import java.util.List;

public class CycleResult<T> {
    private final boolean hasCycle;
    private final List<T> cycle;

    public CycleResult(boolean hasCycle, List<T> cycle) {
        this.hasCycle = hasCycle;
        this.cycle = cycle;
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public List<T> getCycle() {
        return cycle;
    }
}