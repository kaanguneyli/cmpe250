import java.util.LinkedList;

public class ReadyQueue {
    LinkedList<Flight> queue;

    ReadyQueue(){
        this.queue = new LinkedList<>();
    }
    public void enqueue(Flight flight) {
        queue.add(flight);
    }
    public void dequeue() {
        if (!queue.isEmpty())
            queue.removeFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Flight peek() {
        return queue.getFirst();
    }

    public Flight poll() {
        return queue.removeFirst();
    }
}
