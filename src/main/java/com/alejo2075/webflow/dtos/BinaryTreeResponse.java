package com.alejo2075.webflow.dtos;

import com.alejo2075.webflow.datastrutures.BinaryTree.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinaryTreeResponse {

    private Node node;
}
