import java.io.FileWriter;
import java.io.IOException;

public class AvlTree extends BinarySearchTree {
    private static final int BALANCE = 1;
    public AvlTree() {
        root = null;
    }

    public AvlTree(String rootElem) {
        Node root = new Node(rootElem);
        this.root = root;
    }

    @Override
    public void add(String elem, FileWriter fileWriter) throws IOException {
        root = add(elem, root, fileWriter);
    }
    public Node add(String elem, Node node, FileWriter fileWriter) throws IOException {
        // WORKS TOTALLY THE SAME WAY WITH BST ADD METHOD, JUST RETURNS THE BALANCED TREE
        if (node == null) {
            return new Node(elem);
        } else {
            write(node.element + ": New node being added with IP:" + elem + "\n", fileWriter);
            int compare = elem.compareTo(node.element);
            if (compare < 0) {
                node.left = add(elem, node.left, fileWriter);
            } else if (compare > 0) {
                node.right = add(elem, node.right, fileWriter);
            }
            return balance(node, fileWriter);
        }
    }

    @Override
    public void delete(String elem, FileWriter fileWriter) throws IOException {
        if (elem.equals(root.element))
            return;
        root = delete(elem, root, null, fileWriter);
    }


    private Node delete(String elem, Node node, Node parent, FileWriter fileWriter) throws IOException {
        // WORKS TOTALLY THE SAME WAY WITH BST ADD METHOD, JUST RETURNS THE BALANCED TREE
        int compare = elem.compareTo(node.element);
        if (compare < 0) {
            node.left = delete(elem, node.left, node, fileWriter);
        } else if (compare > 0) {
            node.right = delete(elem, node.right, node, fileWriter);
        } else {
            if (node.right == null || node.left == null) { //ONE CHILD OR LEAF
                node = node.right == null ? node.left : node.right;
                if (node == null) {
                    if (log) {
                        write(parent.element + ": Leaf Node Deleted: " + elem + "\n", fileWriter);
                    }

                } else {
                    if (log) {
                        write(parent.element + ": Node with single child Deleted: " + elem + "\n", fileWriter);
                    }
                }
            } else {
                node.element = findMinElement(node.right);
                // WRITE METHOD IS TAKEN ABOVE INNER DELETE TO ARRANGE THE ORDER OF LOGGING
                write(parent.element + ": Non Leaf Node Deleted; removed: " + elem + " replaced: " + node.element + "\n", fileWriter);
                log = false;
                node.right = delete(node.element, node.right, parent, fileWriter);
                log = true;
            }
        }
        return balance(node, fileWriter);
    }


private Node balance(Node node, FileWriter fileWriter) throws IOException {
        if (node == null)
            return node;
        // IF THE RIGHT TREE IS HEAVIER, WE NEED TO FIX
        if (Height(node.right) - Height(node.left) > BALANCE) {
            // IF THE RIGHT OF RIGHT IS HEAVIER, WE HAVE TO MAKE LEFT ROTATION
            if (Height(node.right.right) >= Height(node.right.left)) {
                write("Rebalancing: left rotation" + "\n", fileWriter);
                node = rightRightRotation(node);
            // IF THE LEFT OF RIGHT IS HEAVIER, WE HAVE TO MAKE RIGHT-LEFT ROTATION
            } else {
                write("Rebalancing: right-left rotation" + "\n", fileWriter);
                node = rightLeftRotation(node);
            }
        // SAME THING FOR THE LEFT TREE
        } else if (Height(node.left) - Height(node.right) > BALANCE) {
            if (Height(node.left.left) >= Height(node.left.right)) {
                write("Rebalancing: right rotation" +"\n", fileWriter);
                node = leftLeftRotation(node);
            } else {
                write("Rebalancing: left-right rotation" + "\n", fileWriter);
                node = leftRightRotation(node);
            }
        }
        // UPDATE THE HEIGHT OF THE NODE
        node.height = assignHeight(node);
        return node;
    }

    private Node rightRightRotation(Node node) {
        // ITEMS WILL BE CALLED BY THEIR NAMES IN THE LESSON SLIDES (node is k1)
        Node right_ = node.right; //k2
        node.right = right_.left; //assign left child of k2 to k1's right
        right_.left = node; //assign k1 as k2's left child
        node.height = assignHeight(node); //update heights
        right_.height = assignHeight(right_);
        return right_; //return k2 to replace k1
    }

    private Node leftLeftRotation(Node node) {
        //SYMMETRIC OF RIGHT-RIGHT
        Node left_ = node.left;
        node.left = left_.right;
        left_.right = node;
        node.height = assignHeight(node);
        left_.height = assignHeight(left_);
        return left_;
    }

    private Node rightLeftRotation(Node node) {
        //node=k1, node.right=k3, node.right.left=k2
        // FIRSTLY PERFORM LEFT ROTATION BETWEEN K3 AND K2
        node.right = leftLeftRotation(node.right);
        // NOW DO THE RIGHT ROTATION BETWEEN K2 AND K1
        return rightRightRotation(node);
    }

    private Node leftRightRotation(Node node) {
        // SYMMETRICAL OF RIGHT-LEFT
        node.left = rightRightRotation(node.left);
        return leftLeftRotation(node);
    }

    private int assignHeight(Node node) {
        // FIND THE HIGHEST OF RIGHT SUBTREE AND LEFT SUBTREE AND ADD 1 MORE
        return Height(node.left) > Height(node.right) ? Height(node.left) + 1 : Height(node.right) + 1;
    }

    private int Height(Node node) {
        // ASSIGN -1 IF THE NODE IS NULL, THAT WILL ENABLE US TO MAKE ROTATION WHEN THE OTHER SUBTREE HAS HEIGHT 1
        if (node == null)
            return -1;
        return node.height;
    }

}
