import java.util.ArrayList;
import java.util.PriorityQueue;

public class ACC {
    String id;
    ATC[] atcHash;
    ArrayList<Integer> airportCodesIndexes;
    ReadyQueue readyQueue;
    ArrayList<Flight> waitingLine;
    PriorityQueue<Flight> admitLine;
    int flightNumber;
    int flightsCompleted = 0;

    ACC(String id) {
        this.id = id;
        this.atcHash = new ATC[1000];
        this.airportCodesIndexes = new ArrayList<>(); //holds the indexes in atcHash which are not null
        this.readyQueue = new ReadyQueue();
        this.waitingLine = new ArrayList<>();
        this.admitLine = new PriorityQueue<>(new MyFlightComparator()); //holds the flights which are not admitted yet and which are sent to the readyQueue
    }

    public void accProcess(int timeSkip, int time) {
        //put the flights from admitLine to readyQueue which have to be put
        while (!admitLine.isEmpty() && admitLine.peek().admissionTime <= time) {
            Flight item = admitLine.poll();
            item.lessPriority = false;
            readyQueue.enqueue(item);
        }
        if (!readyQueue.isEmpty()) {
            //take the first flight in queue and decrease its operation time
            Flight flight = readyQueue.peek();
            flight.operationTimes[flight.stage] -= timeSkip;
            int item = flight.operationTimes[flight.stage];
            if (item == 0) {
                //if the process of flight is done reset the accTime, increase the stage and send it to where it belongs
                flight.accTime = 0;
                flight.stage++;
                readyQueue.poll();
                int stage = flight.stage;
                if (stage == 1 || stage == 11) {
                    flight.notYet = true;
                    waitingLine.add(flight);
                } else if (stage == 3) {
                    //find the atc which the flight belongs
                    for (int index : airportCodesIndexes) {
                        ATC atc = atcHash[index];
                        if (atc.airportCode.equals(flight.departureAirportCode)) {
                            //increase the admission time to prevent the flight from being processed twice in this time slice
                            flight.admissionTime = time + timeSkip;
                            atc.admitLine.add(flight);
                            break;
                        }
                    }
                } else if (stage == 13) {
                    //same as 11 but for arrival airport
                    for (int index : airportCodesIndexes) {
                        ATC atc = atcHash[index];
                        if (atc.airportCode.equals(flight.arrivalAirportCode)) {
                            flight.admissionTime = time + timeSkip;
                            atc.admitLine.add(flight);
                            break;
                        }
                    }
                } else {
                    //flight has completed everything
                    flightsCompleted++;
                }
            } else {
                flight.accTime += timeSkip;
                if (flight.accTime == 30) {
                    //if the flight has spent 30t being processed, send it backwards while it is marked to be less prioritised
                    flight.accTime = 0;
                    flight.lessPriority = true;
                    flight.admissionTime = time + timeSkip;
                    readyQueue.poll();
                    admitLine.add(flight);
                }
            }
        }

        for (int i = 0; i < waitingLine.size(); i++) {
            //process all the flights which are waiting
            Flight flight = waitingLine.get(i);
            if (!flight.notYet) {
                flight.operationTimes[flight.stage] -= timeSkip;
                if (flight.operationTimes[flight.stage] == 0) {
                    //if the flight has completed this process, send it back to readyQueue
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
