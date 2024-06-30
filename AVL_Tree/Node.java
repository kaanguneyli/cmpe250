public class Node {
    String element;
    Node left;
    Node right;
    int height;

    public Node() {
        element = null;
        right = null;
        left = null;
    }

    public Node(String element) {
        this.element = element;
        right = null;
        left = null;
    }

    public Node(String element, Node left, Node right) {
        this.element = element;
        this.right = right;
        this.left = left;
    }
}
