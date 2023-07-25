package com.alejo2075.webflow.algorithms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsertionSortAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sort(int[] arr, int delay) {

    }


}
