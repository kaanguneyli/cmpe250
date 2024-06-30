import java.util.Comparator;

public class MyFlightComparator implements Comparator<Flight> {
    @Override
    //if a flight shall be added to readyQueue in an earlier time, put it forward
    //if this is equal, but one of the flights has sent from acc queue to the end put it backwards
    //if this is equal, put the flight with smaller flightCode forward
    public int compare(Flight o1, Flight o2) {
        if (o1.admissionTime < o2.admissionTime)
            return -1;
        else if (o1.admissionTime > o2.admissionTime)
            return 1;
        else {
            if (o1.lessPriority && !o2.lessPriority)
                return 1;
            else if (o2.lessPriority && !o1.lessPriority)
                return -1;
            else if (o1.flightCode.compareTo(o2.flightCode) < 0)
                return -1;
            else return 1;
        }
    }
}
