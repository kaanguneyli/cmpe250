import java.io.*;
import java.util.*;

public class project4 {
    public static void main(String[] args) throws IOException {
        //long begin = System.currentTimeMillis();
        //read the file
        //File file = new File(args[0]);
        File file = new File("C:\\Users\\legıon\\Desktop\\input\\inp3.in");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();

        Node[] nodes = new Node[Integer.parseInt(line)]; // the array that will keep the nodes

        String[] startFinish = new String[2]; // contains start and finish points
        HashSet<String> flags = new HashSet<>(); // will keep the flag names
        HashMap<String, Integer> nodesCreated = new HashMap<>(); // will keep the name of nodes that we have created
        int startFlag = 0; // will be the flag which we will start searching
        int flagNumber = 0; // will be the number of flags
        int start = 0, finish = 0; // will be the start and finishing points of race
        for (int i = 1, id = 0; ; i++) { // id will be the index of the node created in nodes and distances arrays
            // input taking part
            line = reader.readLine();
            if (line == null)
                break;
            if (i == 1)
                flagNumber = Integer.parseInt(line);
            else if (i == 2)
                startFinish = line.split(" ");
            else if (i == 3)
                flags = new HashSet<>(Arrays.asList(line.split(" ")));
            else {
                String[] lineArr = line.split(" ");
                String nodeName = lineArr[0];
                Node node0;
                if (!nodesCreated.containsKey(nodeName)) { // if the node at the beginning of the line was not created before
                    if (nodeName.equals(startFinish[0]))
                        start = id;
                    else if (nodeName.equals(startFinish[1]))
                        finish = id;
                    node0 = new Node(id);
                    nodes[id] = node0;
                    nodesCreated.put(nodeName, id);
                    if (flags.contains(nodeName)) { // id the node is a flag mark it as a flag
                        node0.isFlag = true;
                        startFlag = id;
                    }
                    id++;
                } else // if the node was created before, find it in the nodes array
                    node0 = nodes[nodesCreated.get(nodeName)];
                for (int j = 1; j < lineArr.length; j += 2) { // iterate through the other nodes in the line and do the same thing
                    String nodeName1 = lineArr[j];
                    Node node1;
                    if (!nodesCreated.containsKey(nodeName1)) {
                        if (nodeName1.equals(startFinish[0]))
                            start = id;
                        else if (nodeName1.equals(startFinish[1]))
                            finish = id;
                        node1 = new Node(id);
                        nodes[id] = node1;
                        nodesCreated.put(nodeName1, id);
                        if (flags.contains(nodeName1)) {
                            node1.isFlag = true;
                            startFlag = id;
                        }
                        id++;
                    } else
                        node1 = nodes[nodesCreated.get(nodeName1)];
                    // add the nodes to each other's adjacency lists
                    int d = Integer.parseInt(lineArr[j+1]);
                    node0.adjacentNodes.put(node1, d);
                    node1.adjacentNodes.put(node0, d);
                }
            }
        }
        reader.close();
        String res = shortestPath(nodes, start, finish) +
                "\n" +
                flagCut(startFlag, nodes, flagNumber);
        //FileWriter fileWriter = new FileWriter(args[1]);
        FileWriter fileWriter = new FileWriter("C:\\Users\\legıon\\Desktop\\out.txt");
        fileWriter.write(res);
        fileWriter.close();
        //long end = System.currentTimeMillis();
        //System.out.println(end-begin);
    }

    public static int shortestPath(Node[] nodes, int source, int target) {
        // create the distances array and priority queue which will make us sure that we are going on the shortest path
        PriorityQueue<MyPair> heap = new PriorityQueue<>();
        int[] distances = new int[nodes.length];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;
        heap.add(new MyPair(0, source));
        while (!heap.isEmpty()) {
            MyPair pair = heap.poll();
            Node parent = nodes[pair.nodeId];
            int length = pair.distance;
            for (HashMap.Entry<Node, Integer> item: parent.adjacentNodes.entrySet()) {
                // we take the distance of parent and add the edge length to find the child's distance
                int dist = item.getValue() + length;
                int index = item.getKey().id;
                // if the result is smaller than the previous distance of node, we update and add the node priority queue
                if (distances[index] > dist) {
                    distances[index] = dist;
                    heap.add(new MyPair(dist, index));
                }
            }
        }
        if (distances[target] == Integer.MAX_VALUE)
            return -1;
        return distances[target];
    }

    public static int flagCut(int startFlag, Node[] nodes, int flagNumber) {
        // almost the same thing as shortest path
        int result = 0;
        int[] distances = new int[nodes.length];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startFlag] = 0;
        PriorityQueue<MyPair> heap = new PriorityQueue<>();
        heap.add(new MyPair(0, startFlag));
        HashSet<Integer> visited = new HashSet<>(); // will keep the flag id's which are visited
        while (! heap.isEmpty()) {
            MyPair pair = heap.poll();
            // if the distance in this pair is longer than the current distance of node don't mind
            while (pair.distance > distances[pair.nodeId]) {
                pair = heap.poll();
                if (pair == null) {
                    if (visited.size() < flagNumber)
                        return -1;
                    return result;
                }
            }
            Node parent = nodes[pair.nodeId];
            int length = pair.distance;
            if (parent.isFlag) { // if the node is a flag add it to visited and increase the result
                int id = parent.id;
                result += distances[id];
                distances[id] = 0; // set the distance of the flag to 0, bcs. getting there has no cost anymore
                visited.add(id);
                if (visited.size() == flagNumber)
                    return result;
                length = 0;
            }
            for (HashMap.Entry<Node, Integer> childNodeInt: parent.adjacentNodes.entrySet()) {
                int dist = childNodeInt.getValue() + length;
                int index = childNodeInt.getKey().id;
                if (distances[index] > dist) {
                    if (!visited.contains(index)) {
                        distances[index] = dist;
                        heap.add(new MyPair(dist, index));
                    }
                }
            }
        }
        if (visited.size() < flagNumber)
            return -1;
        return result;
    }
}
