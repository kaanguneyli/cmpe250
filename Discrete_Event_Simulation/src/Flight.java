public class Flight {
    int admissionTime;
    String flightCode;
    ACC acc;
    String departureAirportCode;
    String arrivalAirportCode;
    int[] operationTimes;
    int stage; //refers to the operation to be done next, all the operation times before this index should be 0
    boolean lessPriority; //is true when the flight sent backwards in acc readyQueue
    boolean notYet; //is kept false when we have to prevent the flight from being processed twice in one timeSkip
    int accTime; //if this is 30, flight has to be sent bacwards in acc readyQueue

    Flight (String admissionTime, String flightCode, ACC acc, String departureAirportCode, String arrivalAirportCode) {
        this.admissionTime = Integer.parseInt(admissionTime);
        this.flightCode = flightCode;
        this.acc = acc;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.operationTimes = new int[21];
        this.stage = 0;
        this.lessPriority = false;
        this.notYet = false;
        this.accTime = 0;
    }
}
