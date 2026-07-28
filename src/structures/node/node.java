package structures.node;

<<<<<<< HEAD
public class Node<T> {

    
    
}
=======
import java.util.Objects;

public class Node<T> {

    private final T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (obj == null || getClass() != obj.getClass()) 
            return false;

        Node<?> otro = (Node<?>) 
        obj;
        return Objects.equals(this.data, otro.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "null";
    }
}
>>>>>>> c7a6e64fd8ed285d36c12e504442c2acc3998592
