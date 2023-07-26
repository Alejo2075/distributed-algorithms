package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class LinearSearchAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void search(int[] arr, int target, int delay) throws InterruptedException {
        SearchResponse response = new SearchResponse();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                response.setIndex(i);
                response.setTarget(true);
                messagingTemplate.convertAndSend("/topic/linearSearch", response);
                Thread.sleep(delay);
            }
            response.setTarget(false);
        }
        if(!response.isTarget()){
            messagingTemplate.convertAndSend("/topic/linearSearch", -1);
        }
    }
}
