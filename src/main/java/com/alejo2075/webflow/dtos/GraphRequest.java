package com.alejo2075.webflow.dtos;

import com.alejo2075.webflow.datastrutures.Graph;
import com.alejo2075.webflow.datastrutures.Graph.Vertex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraphRequest {

    private Graph graph;
    private Vertex root;
    private int delay;
}
