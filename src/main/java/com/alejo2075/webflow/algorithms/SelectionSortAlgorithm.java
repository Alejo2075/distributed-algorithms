package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectionSortAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sort(int[] arr, int delay){
        for (int i = 0; i < arr.length - 1; i++) {
            SortResponse response = new SortResponse();
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                response = new SortResponse(minIndex, j);
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
                messagingTemplate.convertAndSend("/topic/selectionSort", response);
            }
            response = new SortResponse(minIndex, i, true);
            messagingTemplate.convertAndSend("/topic/selectionSort", response);
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
        messagingTemplate.convertAndSend("/topic/selectionSort", arr);
    }
}
