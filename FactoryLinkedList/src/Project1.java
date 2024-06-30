import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Project1 {
    public static void main(String args[]) {
        FactoryImpl factory = new FactoryImpl();
        ArrayList<String[]> arrays = new ArrayList<>();

        try {
            File file = new File(args[0]);
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                String[] array = data.split(" ");
                arrays.add(array);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String output = "";
        for (String[] array: arrays) {
            try {
                if (array[0].equals("AF")) {
                    Product product = new Product(Integer.parseInt(array[1]), Integer.parseInt(array[2]));
                    factory.addFirst(product);
                } else if (array[0].equals("AL")) {
                    Product product = new Product(Integer.parseInt(array[1]), Integer.parseInt(array[2]));
                    factory.addLast(product);
                } else if (array[0].equals("A")) {
                    Product product = new Product(Integer.parseInt(array[2]), Integer.parseInt(array[3]));
                    factory.add(Integer.parseInt(array[1]), product);
                } else if (array[0].equals("RF")) {
                    output += factory.removeFirst().toString() + "\n";
                } else if (array[0].equals("RL")) {
                    output += factory.removeLast().toString() + "\n";
                } else if (array[0].equals("RI")) {
                    output += factory.removeIndex(Integer.parseInt(array[1])).toString() + "\n";
                } else if (array[0].equals("RP")) {
                    output += factory.removeProduct(Integer.parseInt(array[1])).toString() + "\n";
                } else if (array[0].equals("F")) {
                    output += factory.find(Integer.parseInt(array[1])).toString() + "\n";
                } else if (array[0].equals("G")) {
                    output += factory.get(Integer.parseInt(array[1])).toString() + "\n";
                } else if (array[0].equals("U")) {
                    output += factory.update(Integer.parseInt(array[1]), Integer.parseInt(array[2])).toString() + "\n";
                } else if (array[0].equals("FD")) {
                    output += factory.filterDuplicates() + "\n";
                } else if (array[0].equals("R")) {
                    factory.reverse();
                    output += factory.toString() + "\n";
                } else if (array[0].equals("P")) {
                    output += factory.toString() + "\n";
                }
            } catch (NoSuchElementException e) {
                output += e.getMessage();
            } catch (IndexOutOfBoundsException r) {
                output += r.getMessage();
            }
        }

        try {
            FileWriter file = new FileWriter(args[1]);
            file.write(output);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}