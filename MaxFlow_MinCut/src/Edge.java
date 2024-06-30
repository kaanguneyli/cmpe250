public class Edge {
    Node node; //the node which the edge ends at
    int weight;
    boolean isForward; //is true if an edge is not a reverse edge created in the ford-fulkerson process

    Edge(Node node, int weight, boolean isForward){
        this.node = node;
        this.weight = weight;
        this.isForward = isForward;
    }
}
