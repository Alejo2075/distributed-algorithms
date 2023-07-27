package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SearchAlgorithms {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void binarySearch(int[] arr, int target, int delay) throws InterruptedException {
        int low = 0;
        int high = arr.length - 1;
        SearchResponse response = new SearchResponse();
        while(low <= high){
            int mid = low + (high - low) / 2;
            response.setIndex(mid);
            if(arr[mid] < target){
                low = mid +1;
                response.setTarget(false);
                messagingTemplate.convertAndSend("/topic/binarySearch", response);
                Thread.sleep(delay);
            } else if (arr[mid] > target) {
                high = mid - 1;
                response.setTarget(false);
                messagingTemplate.convertAndSend("/topic/binarySearch", response);
                Thread.sleep(delay);
            } else {
                response.setTarget(true);
                messagingTemplate.convertAndSend("/topic/binarySearch", response);
            }
        }
        if(!response.isTarget()){
            messagingTemplate.convertAndSend("/topic/binarySearch", -1);
        }
    }

    public void linearSearch(int[] arr, int target, int delay) throws InterruptedException {
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
