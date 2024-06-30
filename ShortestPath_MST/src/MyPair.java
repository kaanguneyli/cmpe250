
public class MyPair implements Comparable<MyPair> {
    int distance; //the value in the distances array
    int nodeId; //index in the nodes/distances array

    MyPair (int distance, int nodeId) {
        this.distance = distance;
        this.nodeId = nodeId;
    }


    @Override
    public int compareTo(MyPair o2) {
        //compares pairs according to distance, if the distances are equal compares according to id
        if (this.distance < o2.distance)
            return -1;
        else if (this.distance > o2.distance)
            return 1;
        else {
            if (this.nodeId < o2.nodeId)
                return -1;
            else if (this.nodeId > o2.nodeId)
                return 1;
            return 0;
        }
    }
}
