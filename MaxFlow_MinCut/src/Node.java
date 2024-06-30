import java.util.HashMap;

public class Node {
    String name;
    int id; //index of the node in the initialAdjacencies and residual arrays
    HashMap<Integer, Edge> adjacencyList; //Integer is the index of the adjacent node, Edge is explained in its own class

    Node (String name, int id) {
        this.name = name;
        this.id = id;
        adjacencyList = new HashMap<>();
    }
}
