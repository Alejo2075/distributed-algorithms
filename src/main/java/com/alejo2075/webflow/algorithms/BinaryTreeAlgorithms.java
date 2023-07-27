package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.datastrutures.BinaryTree.Node;
import com.alejo2075.webflow.dtos.BinaryTreeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class BinaryTreeAlgorithms {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void traverseInOrder(Node node, int delay) {
        if (node != null) {
            traverseInOrder(node.left, delay);
            BinaryTreeResponse response = new BinaryTreeResponse(node);
            messagingTemplate.convertAndSend("/topic/inOrden", response);
            traverseInOrder(node.right, delay);
        }
    }

    public void traversePreOrder(Node node, int delay) {
        if (node != null) {
            BinaryTreeResponse response = new BinaryTreeResponse(node);
            messagingTemplate.convertAndSend("/topic/preOrden", response);
            traversePreOrder(node.left, delay);
            traversePreOrder(node.right, delay);
        }
    }

    public void traversePostOrder(Node node, int delay) {
        if (node != null) {
            traversePostOrder(node.left, delay);
            traversePostOrder(node.right, delay);
            BinaryTreeResponse response = new BinaryTreeResponse(node);
            messagingTemplate.convertAndSend("/topic/postOrden", response);
        }
    }

    public void traverseLevelOrder(Node root, int delay) {
        if (root == null) {
            return;
        }

        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);

        while (!nodes.isEmpty()) {

            Node node = nodes.remove();

            BinaryTreeResponse response = new BinaryTreeResponse(node);
            messagingTemplate.convertAndSend("/topic/levelOrden", response);

            if (node.left != null) {
                nodes.add(node.left);
            }

            if (node.right != null) {
                nodes.add(node.right);
            }
        }
    }
}
