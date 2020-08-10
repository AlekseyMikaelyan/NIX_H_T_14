package org.example.dijkstradatabaseproject.dijkstradatabaseprojectutil;

public class Edge {

    public final int target;
    public final int weight;

    public Edge(int target, int weight) {
        this.target = target;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "target=" + target +
                ", weight=" + weight +
                '}';
    }
}
