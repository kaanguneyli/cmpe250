import java.util.HashMap;

public class Node {
    int id; // the index of node in nodes and distances arrays
    HashMap<Node, Integer> adjacentNodes; // stores adjacentNodes and weights of edges
    boolean isFlag = false; // true if the node is a flag
    Node (int id) {
        this.id = id;
        adjacentNodes = new HashMap<>();
    }
}
