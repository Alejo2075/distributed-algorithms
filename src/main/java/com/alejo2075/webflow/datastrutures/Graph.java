package com.alejo2075.webflow.datastrutures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Graph {

    private Map<Vertex, List<Vertex>> adjVertices;
    public class Vertex {
        public int label;
        Vertex(int label) {
            this.label = label;
        }
    }

    void addVertex(int label) {
        adjVertices.putIfAbsent(new Vertex(label), new ArrayList<>());
    }

    void removeVertex(int label) {
        Vertex v = new Vertex(label);
        adjVertices.values().stream().forEach(e -> e.remove(v));
        adjVertices.remove(new Vertex(label));
    }

    void addEdge(int label1, int label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        adjVertices.get(v1).add(v2);
        adjVertices.get(v2).add(v1);
    }

    void removeEdge(int label1, int label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        List<Vertex> eV1 = adjVertices.get(v1);
        List<Vertex> eV2 = adjVertices.get(v2);
        if (eV1 != null)
            eV1.remove(v2);
        if (eV2 != null)
            eV2.remove(v1);
    }

    public List<Vertex> getAdjVertices(Vertex vertex) {
        return adjVertices.get(vertex);
    }

    public Map<Vertex, List<Vertex>> getAdjVertices() {
        return adjVertices;
    }

    public int getIndexOf(Vertex vertex){
        List<Vertex> vertices = new ArrayList<>(adjVertices.keySet());
        return vertices.indexOf(vertex);
    }
}
