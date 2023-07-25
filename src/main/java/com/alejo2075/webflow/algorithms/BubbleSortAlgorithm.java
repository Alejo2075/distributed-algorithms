package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class BubbleSortAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sort(int[] arr, int delay) throws InterruptedException {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                SortResponse response = new SortResponse(j, j+1);
                if (arr[j] > arr[j + 1]) {
                    response.setAreChange(true);
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
                messagingTemplate.convertAndSend( "/topic/bubbleSort", response);
                Thread.sleep(delay);
            }
        }
        messagingTemplate.convertAndSend("/topic/bubbleSort", arr);
    }
}
