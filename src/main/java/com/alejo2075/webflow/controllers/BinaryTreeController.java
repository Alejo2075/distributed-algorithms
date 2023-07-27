package com.alejo2075.webflow.controllers;

import com.alejo2075.webflow.algorithms.BinaryTreeAlgorithms;
import com.alejo2075.webflow.dtos.BinaryTreeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class BinaryTreeController {

    @Autowired
    private BinaryTreeAlgorithms binaryTreeAlgorithms;

    @MessageMapping("/inOrder")
    public void inOrder(@Payload BinaryTreeRequest request){
        binaryTreeAlgorithms.traverseInOrder(request.getNode(), request.getDelay());
    }

    @MessageMapping("/preOrder")
    public void preOrder(@Payload BinaryTreeRequest request){
        binaryTreeAlgorithms.traversePreOrder(request.getNode(), request.getDelay());
    }

    @MessageMapping("/postOrder")
    public void postOrder(@Payload BinaryTreeRequest request){
        binaryTreeAlgorithms.traversePostOrder(request.getNode(), request.getDelay());
    }

    @MessageMapping("/levelOrder")
    public void levelOrder(@Payload BinaryTreeRequest request){
        binaryTreeAlgorithms.traverseLevelOrder(request.getNode(), request.getDelay());
    }
}
