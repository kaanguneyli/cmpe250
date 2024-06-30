import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BinarySearchTree {
    Node root;
    boolean log = true;

    public BinarySearchTree() {
        root = null;
    }
    public BinarySearchTree(String rootElem) {
        Node root = new Node(rootElem);
        this.root = root;
    }

    //ADD
    public void add(String elem, FileWriter fileWriter) throws IOException {
        // WE WILL USE THE ADD AND DELETE METHOD'S IN THIS MANNER, BECAUSE WE HAVE ASSIGN THE CHANGES WE MADE IN THE TREE TO THE ROOT
        // THIS IS CAUSED BY THE RECURSIVE MANNER, ALSO THE ROOT ACTUALLY REPRESENT THE WHOLE TREE
        root = add(elem, root, fileWriter);
    }
    private Node add(String elem, Node node, FileWriter fileWriter) throws IOException {
        // IF YOU REACH A NULL NODE CREATE A NEW NODE AND RETURN
        if (node == null) {
            return new Node(elem);
        } else {
            // COMPARE THE NEW NODE TO EXISTING NODES, IF NEW NODE IS SMALLER GO TO LEFT TREE, ELSE GO TO RIGHT TREE AND ASSIGN
            write(node.element + ": New node being added with IP:" + elem + "\n", fileWriter);
            int compare = elem.compareTo(node.element);
            if (compare < 0)
                node.left = add(elem, node.left, fileWriter);
            else if (compare > 0)
                node.right = add(elem, node.right, fileWriter);
            return node;
        }
    }

    public void delete(String elem, FileWriter fileWriter) throws IOException {
        root = delete(elem, root, null, fileWriter);
    }
    private Node delete(String elem, Node node, Node parent, FileWriter fileWriter) throws IOException {
        // FIND THE NODE WITH THE SAME WAY AS ADD METHOD
        int compare = elem.compareTo(node.element);
        if (compare < 0) {
            node.left = delete(elem, node.left, node, fileWriter);
        } else if (compare > 0) {
            node.right = delete(elem, node.right, node, fileWriter);
        } else {
            // WHEN YOU FIND THE NODE THERE ARE 3 CASES
            // CASE 1 AND 2: 0-1 CHILD
            // IF THE NODE HAS A CHILD IT WILL BE ASSIGNED TO THE NODE ELSE NULL WILL BE ASSIGNED
            if (node.right == null || node.left == null) {
                node = node.right == null ? node.left : node.right;
                // LOG THE DELETION IF IT NOT CAUSED BY REPLACEMENT
                if (node == null) {
                    if (log)
                        write(parent.element + ": Leaf Node Deleted: " + elem + "\n", fileWriter);
                } else {
                    if (log)
                        write(parent.element + ": Node with single child Deleted: " + elem + "\n", fileWriter);
                }
            } else {
                // FIND THE MIN ELEMENT FROM RIGHT SUBTREE, DELETE IT (WITHOUT LOGGING) AND ASSIGN THE VALUE TO THE NODE
                String item = findMinElement(node.right);
                log = false;
                delete(item, fileWriter);
                log = true;
                node.element = item;
                write(parent.element + ": Non Leaf Node Deleted; removed: " + elem + " replaced: " + item + "\n", fileWriter);
            }
        }
        return node;
    }


    public String findMinElement(Node node) {
        // UNTIL YOU REACH A NODE WITHOUT A CHILD, WALK TOWARDS THE LEFT SUBTREE
        if (node.left == null)
            return node.element;
        else
            return findMinElement(node.left);
    }

    public String sendMessage(String sender, String receiver) {
        // FIND A WAY FROM ROOT TO SENDER
        ArrayList<String> rootToSender = new ArrayList<>();
        findWay(root, sender, rootToSender);
        // FIND A WAY FROM ROOT TO RECEIVER
        ArrayList<String> rootToReceiver = new ArrayList<>();
        findWay(root, receiver, rootToReceiver);
        // ITERATE THROUGH SENDER FROM THE ROOT, WHEN YOU SEE AN ELEMENT WHICH IS CONTAINED IN THE OTHER LIST, STOP AND
        // USE THIS POINT AS THE POINT YOU START GOING DOWNWARDS IN THE TREE.
        // TAKE THE INDEXES OF THE DIRECTION CHANGING POINT (C-POINT) FROM LISTS
        int idx1 = 0;
        int idx2 = 0;
        for (int i=rootToSender.size()-1; i>=0; i--) {
            String currentItem = rootToSender.get(i);
            if (rootToReceiver.contains(currentItem)) {
                idx1 = i;
                idx2 = rootToReceiver.indexOf(currentItem);
                break;
            }
        }
        // CREATE A STRING AND ADD THE FIRST LOG LINE
        String res = "";
        res += sender + ": Sending message to: " + receiver + "\n";
        // START FROM NODE AND GO TO C-POINT AND LOG (WE WILL LOG THE C-POINT FROM THIS LIST TO NOT LOSE THE NODE THAT TRANMISSION IS DONE FROM)
        for (int i=rootToSender.size()-2; i>=idx1; i--) {
            String item = rootToSender.get(i);
            // BREAK IF THE RECEIVER IS IN THIS LIST
            if (item.equals(receiver))
                break;
            res += item + ": Transmission from: " + rootToSender.get(i + 1) + " receiver: " + receiver + " sender:" + sender + "\n";
        }
        //START FROM THE NODE ONE AFTER THE C-POINT AND GO UNTIL THE RECEIVER
        for (int i=idx2+1; i<rootToReceiver.size()-1; i++)
            res += rootToReceiver.get(i) + ": Transmission from: " + rootToReceiver.get(i - 1) + " receiver: " + receiver + " sender:" + sender + "\n";
        // LOG THE LAST LINE
        res += receiver + ": Received message from: " + sender + "\n";
        return res;
    }

    public ArrayList<String> findWay (Node start, String end, ArrayList<String> arrayList) {
        // IF ITEM IS SMALLER, GO TO LEFT TREE. IF IT IS LARGER, GO TO RIGHT TREE. IF IT IS EQUAL RETURN THE LIST
        arrayList.add(start.element);
        int compare = end.compareTo(start.element);
        if (compare == 0)
            return arrayList;
        else if (compare < 0)
            return findWay(start.left, end, arrayList);
        else
            return findWay(start.right, end, arrayList);
    }

    public void write(String str, FileWriter fileWriter) throws IOException {
        // WRITE THE GIVEN STRING TO THE GIVEN FILE
        fileWriter.write(str);
    }
}
