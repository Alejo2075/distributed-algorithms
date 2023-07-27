package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.datastrutures.Graph;
import com.alejo2075.webflow.datastrutures.Graph.Vertex;
import com.alejo2075.webflow.dtos.GraphResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphAlgorithms {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void breadthFirstTraversal(Graph graph, Vertex root, int delay) {
        Set<Vertex> visited = new LinkedHashSet<Vertex>();
        Queue<Vertex> queue = new LinkedList<Vertex>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();
            for (Vertex v : graph.getAdjVertices(vertex)) {
                if (!visited.contains(v)) {
                    GraphResponse response = new GraphResponse(v, delay);
                    messagingTemplate.convertAndSend("/topic/bfs", response);
                    visited.add(v);
                    queue.add(v);
                }
            }
        }
    }

    public void depthFirstTraversal(Graph graph, Vertex root, int delay) {
        boolean[] isVisited = new boolean[graph.getAdjVertices().size()];
        dfsRecursive(graph, root, isVisited, delay);
    }

    private void dfsRecursive(Graph graph, Vertex current, boolean[] isVisited, int delay) {
        isVisited[graph.getIndexOf(current)] = true;
        GraphResponse response = new GraphResponse(current ,delay);
        messagingTemplate.convertAndSend("/topic/dfs");
        for (Vertex neighbor : graph.getAdjVertices().get(current)) {
            if (!isVisited[graph.getIndexOf(neighbor)])
                dfsRecursive(graph, neighbor, isVisited, delay);
        }
    }
}
