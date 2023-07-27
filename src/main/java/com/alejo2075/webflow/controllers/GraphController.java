package com.alejo2075.webflow.controllers;

import com.alejo2075.webflow.algorithms.GraphAlgorithms;
import com.alejo2075.webflow.dtos.GraphRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class GraphController {

    @Autowired
    private GraphAlgorithms graphAlgorithms;

    @MessageMapping("/bfs")
    public void bfs(@Payload GraphRequest request){
        graphAlgorithms.breadthFirstTraversal(request.getGraph(), request.getRoot(), request.getDelay());
    }

    @MessageMapping("/dfs")
    public void dfs(@Payload GraphRequest request){
        graphAlgorithms.depthFirstTraversal(request.getGraph(), request.getRoot(), request.getDelay());
    }

    @MessageMapping("/dijstra")
    public void dijstra(@Payload GraphRequest request){
        graphAlgorithms.depthFirstTraversal(request.getGraph(), request.getRoot(), request.getDelay());
    }
}
