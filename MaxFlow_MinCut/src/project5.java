import java.io.*;
import java.util.*;

public class project5 {

    public static void main(String[] args) throws IOException {

        //input taking part
        File file = new File(args[0]);
        //File file = new File("C:\\Users\\legıon\\Desktop\\emre_inputs\\input_11.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int cityNumber = Integer.parseInt(reader.readLine()); //line 0
        Node[] residual = new Node[cityNumber + 8]; //this array will represent our residual graph by keeping the nodes
        String[] regionCapacities = reader.readLine().split(" ");
        Node source = new Node("s", 0);
        residual[0] = source;
        HashMap<String, Integer> nodesCreated = new HashMap<>();
        int nextCityIndex = 7;
        int kl = 0;
        ArrayList<HashMap<Integer, String>> initialAdjacencies = new ArrayList<>(cityNumber + 8);//this will represent the original graph by holding the initial adjacency lists of all nodes
        for (int i=0; i<cityNumber+8; i++) {
            initialAdjacencies.add(new HashMap<>());
        }
        for (int i=1; i<7; i++) {
            String[] line = reader.readLine().split(" ");
            Node item = new Node(line[0], i);
            nodesCreated.put(line[0], i);
            Node child = null;
            //while iterating through the input lines if you have a node you have created, just add its edges; else create the node and add the edges
            for (int j=1; j<line.length; j+=2) {
                String line_j = line[j];
                if (nodesCreated.containsKey(line_j)) {
                    child = residual[nodesCreated.get(line_j)];
                } else {
                    if (line_j.equals("KL"))
                        kl = nextCityIndex; //this will keep the index of KL
                    child = new Node(line_j, nextCityIndex);
                    nodesCreated.put(line_j, nextCityIndex);
                    residual[nextCityIndex] = child;
                    nextCityIndex++;
                }
                item.adjacencyList.put(child.id, new Edge(child, Integer.parseInt(line[j+1]), true));
                initialAdjacencies.get(i).put(child.id, child.name);
            }
            source.adjacencyList.put(item.id, new Edge(item, Integer.parseInt(regionCapacities[i-1]), true));
            initialAdjacencies.get(0).put(item.id, item.name);
            residual[i] = item;
        }
        for (int i=0; i<cityNumber; i++) {
            String[] line = reader.readLine().split(" ");
            Node node = null;
            if (nodesCreated.containsKey(line[0])) {
                int id = nodesCreated.get(line[0]);
                node = residual[id];
            } else {
                node = new Node(line[0], nextCityIndex);
                nodesCreated.put(line[0], nextCityIndex);
                residual[nextCityIndex] = node;
                nextCityIndex++;
            }
            Node child = null;
            for (int j=1; j<line.length; j += 2) {
                String line_j = line[j];
                if (nodesCreated.containsKey(line_j)) {
                    child = residual[nodesCreated.get(line_j)];
                } else {
                    if (line_j.equals("KL"))
                        kl = nextCityIndex;
                    child = new Node(line_j, nextCityIndex);
                    nodesCreated.put(line_j, nextCityIndex);
                    residual[nextCityIndex] = child;
                    nextCityIndex++;
                }
                node.adjacencyList.put(child.id , new Edge(child, Integer.parseInt(line[j+1]), true));
                initialAdjacencies.get(node.id).put(child.id, child.name);
            }
        }
        reader.close();

        //ford-fulkerson algorithm
        int[] parents = new int[cityNumber + 8]; //this array will keep the parent for each node in bfs
        int flow = 0;
        while (true) {
            //find a path
            ArrayList<Integer> path = bfs(residual, 0, kl, parents);
            if (path == null) {
                break;
            }
            //find path's bottleneck capacity by iterating the edges with the help of parents array
            int bottleneck = Integer.MAX_VALUE;
            for (int i=path.size()-1; i>0; i--) {
                Node node = residual[path.get(i)];
                Edge child = node.adjacencyList.get(path.get(i-1));
                int distance = child.weight;
                if (distance < bottleneck)
                    bottleneck = distance;
            }
            //add the bottleneck capacity to flow
            flow += bottleneck;
            //reorganize the residual graph
            //subtract the bottleneck capacity from the edges on the path
            //create the reverse edges (if they don't exist) and add the bottleneck capacity
            for (int i=path.size()-1; i>0; i--) {
                Node node = residual[path.get(i)];
                Edge child = node.adjacencyList.get(path.get(i-1));
                child.weight -= bottleneck;
                if (child.weight == 0)
                    node.adjacencyList.remove(child.node.id);
                if (! child.node.adjacencyList.containsKey(node.id)) {
                    child.node.adjacencyList.put(node.id, new Edge(node, bottleneck, false));
                } else {
                    child.node.adjacencyList.get(node.id).weight += bottleneck;
                }
            }
        }
        StringBuilder result = new StringBuilder();
        result.append(flow).append("\n");

        //run dfs on the last residual graph and decide which nodes are reachable
        //then with the help initialAdjacencies array find the forward edges which start from a reachable node and go to an unreachable array
        //these edges will give the result for the bonus part
        Stack<Integer> stack = new Stack<>();
        HashSet<Integer> visited = new HashSet<>();
        visited.add(0);
        for (Map.Entry<Integer, Edge> childNode: residual[0].adjacencyList.entrySet()) {
            stack.push(childNode.getKey());
        }
        while (! stack.isEmpty()) {
            int nodeIndex = stack.pop();
            if (!visited.contains(nodeIndex)) {
                visited.add(nodeIndex);
                for (Map.Entry<Integer, Edge> childNode: residual[nodeIndex].adjacencyList.entrySet()) {
                    stack.push(childNode.getKey());
                }
            }
        }
        for (int i: visited) {
            for (Map.Entry<Integer, String> item: initialAdjacencies.get(i).entrySet()) {
                int j = item.getKey();
                if (! visited.contains(j)) {
                    String parentName = residual[i].name;
                    if (!parentName.equals("s"))
                        result.append(residual[i].name).append(" ").append(item.getValue()).append("\n");
                    else
                        result.append(item.getValue()).append("\n");
                }
            }
        }
        FileWriter writer = new FileWriter(args[1]);
        //FileWriter writer = new FileWriter("C:\\Users\\legıon\\Desktop\\out.txt");
        writer.write(result.toString());
        writer.close();
    }



    public static ArrayList<Integer> bfs(Node[] residual, int s, int kl, int[] parents) {
        //bfs algorithm which returns the path in a reverse order and also updades the parents array
        parents[s] = -1;
        ArrayList<Integer> path = new ArrayList<>();
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(s);
        HashSet<Integer> processed = new HashSet<>();
        processed.add(s);
        while (! queue.isEmpty()) {
            Integer parent = queue.removeFirst();
            for (Map.Entry<Integer, Edge> childNodeInt: residual[parent].adjacencyList.entrySet()) {
                int val = childNodeInt.getValue().node.id;
                if (! processed.contains(val)) {
                    processed.add(val);
                    queue.add(val);
                    parents[val] = parent;
                    if (val == kl) {
                        path.add(val);
                        for (int num = parents[val]; num != -1; num = parents[val]) {
                            path.add(num);
                            val = num;
                        }
                        return path;
                    }
                }
            }
        }
        return null;
    }
}