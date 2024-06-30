import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException {

        // READ THE FILE AND ADD THE LINES TO ARRAYLIST AS SEPERATE ARRAYS
        File file = new File(args[0]);
        Scanner input = new Scanner(file);
        ArrayList<String[]> lines = new ArrayList<>();
        while (input.hasNextLine()) {
            lines.add(input.nextLine().split(" "));
        }

        // CREATE THE FILES WHICH WILL BE WRITTEN ON
        String outputFname = args[1];
        File outputBst = new File(outputFname + "_BST.txt");
        outputBst.createNewFile();
        File outputAvl = new File(outputFname + "_AVL.txt");
        outputAvl.createNewFile();
        FileWriter bstWriter = new FileWriter(outputBst);
        FileWriter avlWriter = new FileWriter(outputAvl);

        // CREATE THE TREES USING THE FIRST LINE
        BinarySearchTree bst = new BinarySearchTree(lines.get(0)[0]);
        AvlTree avl = new AvlTree(lines.get(0)[0]);
        lines.remove(0);
        // ITERATE THROUGH THE COMMANDS AND DO THE OPERATIONS
        for (String[] line: lines) {
            if (line[0].equals("ADDNODE")) {
                bst.add(line[1], bstWriter);
                avl.add(line[1], avlWriter);
            } else if (line[0].equals("DELETE")) {
                bst.delete(line[1], bstWriter);
                avl.delete(line[1], avlWriter);
            } else if (line[0].equals("SEND")) {
                // LOG THE SEND MESSAGE
                bstWriter.write(bst.sendMessage(line[1], line[2]));
                avlWriter.write(avl.sendMessage(line[1], line[2]));
            }
        }
        bstWriter.close();
        avlWriter.close();
    }
}
