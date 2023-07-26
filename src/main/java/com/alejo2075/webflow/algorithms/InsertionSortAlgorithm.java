package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsertionSortAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sort(int[] arr, int delay) throws InterruptedException {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            SortResponse response = new SortResponse(i, j, false);
            messagingTemplate.convertAndSend("/topic/insertionSort", response);
            Thread.sleep(delay);
            while (j >= 0 && arr[j] > key) {
                response.setAreChange(true);
                messagingTemplate.convertAndSend("/topic/insertionSort", response);
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = key;
        }
        messagingTemplate.convertAndSend("/topic/insertionSort", arr);
    }


}
