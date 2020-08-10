package org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class GraphHandler {
    public static void computePaths(List<Node> graph, Node source) {

        source.minDistance = 0;
        PriorityQueue<Node> nodesQueue = new PriorityQueue<>();
        nodesQueue.add(source);

        while (!nodesQueue.isEmpty()) {
            Node u = nodesQueue.poll();

            for (Edge e : u.adjacency) {
                int index = e.target;
                Node node = getNodeByIndex(graph, index);
                int weight = e.weight;
                int distanceThroughU = u.minDistance + weight;

                if (distanceThroughU < node.minDistance) {
                    nodesQueue.remove(node);
                    node.minDistance = distanceThroughU ;
                    node.previous = u;
                    nodesQueue.add(node);
                }
            }
        }
    }

    public static List<Node> getShortestPathTo(Node target) {

        List<Node> path = new ArrayList<>();
        for (Node node = target; node != null; node = node.previous)
            path.add(node);

        Collections.reverse(path);
        return path;
    }

    public static Node getNodeByIndex(List<Node> graph, int index) {
        Node result = null;

        for(Node node: graph) {
            if(node.index == index) {
                result = node;
            }
        }

        if(result == null) {
            throw new IllegalArgumentException("узла с таким индексом нет в графе");
        }

        return result;
    }

    public static Node getNodeByName(List<Node> graph, String name) {
        Node result = null;

        for(Node node: graph) {
            if(node.name.equals(name)) {
                result = node;
            }
        }

        if(result == null) {
            throw new IllegalArgumentException("узла с таким индексом нет в графе");
        }

        return result;
    }
}
