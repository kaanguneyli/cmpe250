import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Project3 {
    public static void main(String[] args) throws IOException {
        //read the file and split the lines then put them in an arraylist in order
        long begin = System.currentTimeMillis();
        ArrayList<String[]> lines = new ArrayList<>();
        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().split(" "));
        }
        scanner.close();

        //read the lines that contain accs create the acc objects, and put these accs in an arraylist
        int accNumber = Integer.parseInt(lines.get(0)[0]);
        ArrayList<ACC> accs = new ArrayList<>();
        for (int i=1; i<=accNumber; i++) {
            String[] line = lines.get(i);
            ACC acc = new ACC(line[0]);
            accs.add(acc);
            for (int j=1; j<line.length; j++) {
                //here we just create the acc objects, they don't get lost since they are hashed in constructor
                new ATC(acc, line[j]);
            }
        }

        //read the rest of the lines which contain the flights
        for (int i=accNumber+1; i<lines.size(); i++) {
            String[] line = lines.get(i);
            ACC acc = null;
            //find the acc of the flight
            for (int j=0; j<accs.size(); j++) {
                if (accs.get(j).id.equals(line[2])) {
                    acc = accs.get(j);
                }
            }
            //create the flight object
            Flight flight = new Flight(line[0], line[1], acc, line[3], line[4]);
            //read the operation times and put them in an array in object
            for (int j=5; j<line.length; j++) {
                flight.operationTimes[j-5] = Integer.parseInt(line[j]);
            }
            //add the flight to the acc admitline
            acc.admitLine.add(flight);
            acc.flightNumber++;
        }

        //if an acc does not have any flight to process, log it and remove from the list
        String res = "";
        for (int i=0; i<accs.size(); i++) {
            ACC current = accs.get(i);
            if (current.admitLine.isEmpty()) {
                res += getLog(current, 0)  + "\n";
                accs.remove(current);
                i--;
            }
        }

        //this for loop iterates through accs
        for (int a=0; a<accs.size(); a++) {
            ACC acc = accs.get(a);
            int timeSkip = 1; //we will increase the time by one
            int time = acc.admitLine.peek().admissionTime; //time of the first admission, nothing happens before this time
            //do the time iteration
            while (true) {
                //process the acc and all of its atcs
                acc.accProcess(timeSkip, time);
                for (int i=0; i<acc.airportCodesIndexes.size(); i++) {
                    ATC atc = acc.atcHash[acc.airportCodesIndexes.get(i)];
                    atc.atcProcess(timeSkip, time);
                }
                time += timeSkip;
                //if the acc has completed all of its flights break the loop
                if (acc.flightNumber == acc.flightsCompleted)
                    break;
            }
            //log the acc
            res += getLog(acc, time) + "\n";
        }
        //write the log on the file
        FileWriter fileWriter = new FileWriter(args[1]);
        fileWriter.write(res);
        fileWriter.close();
        long finish = System.currentTimeMillis();
        System.out.println(finish - begin);
    }
    public static String getLog(ACC acc, int time) {
        String ret = "";
        ret += acc.id + " " + time + " ";
        for (int j=0; j<1000; j++) {
            ATC atc = acc.atcHash[j];
            if (atc != null) {
                ret += atc.airportCode + String.format("%03d", j) + " ";
            }
        }
        ret += "\n";
        return ret.strip();
    }
}
