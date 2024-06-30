import java.util.ArrayList;
import java.util.PriorityQueue;

public class ATC {
    ACC center;
    String airportCode;
    String id;
    ReadyQueue readyQueue;
    ArrayList<Flight> waitingLine;
    PriorityQueue<Flight> admitLine;

    ATC(ACC center, String airportCode) {
        this.center = center;
        this.airportCode = airportCode;
        String place = String.format("%03d", placeAirport()); //when the constructor works the atc will be added to hash table in acc
        this.id = center.id.concat(place);
        this.readyQueue = new ReadyQueue();
        this.waitingLine = new ArrayList<>();
        this.admitLine= new PriorityQueue<>(new MyFlightComparator());
    }

    public int placeAirport() {
        //hash the atc in acc and return the index
        int place = 0;
        int len = airportCode.length();
        for (int i=0; i<len; i++)
            place += airportCode.charAt(i) * Math.pow(31, i);
        while (center.atcHash[place%1000] != null) {
            place++;
        }
        place %= 1000;
        center.airportCodesIndexes.add(place);
        center.atcHash[place] = this;
        return place;
    }

    public void atcProcess(int timeSkip, int time) {
        //same thing with accProcess
        while (!admitLine.isEmpty() && admitLine.peek().admissionTime <= time) {
            Flight item = admitLine.poll();
            readyQueue.enqueue(item);
        }
        if (!readyQueue.isEmpty()) {
            Flight flight = readyQueue.peek();
            flight.operationTimes[flight.stage] -= timeSkip;
            int item = flight.operationTimes[flight.stage];
            if (item == 0) {
                readyQueue.poll();
                flight.stage++;
                if (flight.stage == 10 || flight.stage == 20) {
                    //send the flight back to acc
                    flight.admissionTime = time + timeSkip;
                    center.admitLine.add(flight);
                } else {
                    flight.notYet = true;
                    waitingLine.add(flight);
                }
            }
        }
        for (int i = 0; i < waitingLine.size(); i++) {
            Flight flight = waitingLine.get(i);
            if (!flight.notYet) {
                flight.operationTimes[flight.stage] -= timeSkip;
                if (flight.operationTimes[flight.stage] == 0) {
                    flight.stage++;
                    waitingLine.remove(flight);
                    flight.admissionTime = time + timeSkip;
                    admitLine.add(flight);
                    i--;
                }
            } else
                flight.notYet = false;
        }
    }
}
