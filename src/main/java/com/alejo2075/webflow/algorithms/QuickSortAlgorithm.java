package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class QuickSortAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sort(int[] arr, int delay) throws InterruptedException {
        quickSort(arr, 0, arr.length - 1, delay);
        messagingTemplate.convertAndSend("/topic/quickSort", arr);
    }

    private void quickSort(int[] arr, int left, int right, int delay) throws InterruptedException {
        if(left < right) {
            int index = partition(arr, left, right, delay);
            quickSort(arr, left, index - 1, delay);
            quickSort(arr, index + 1, right, delay);
        }
    }

    private int partition(int[] arr, int left, int right, int delay) throws InterruptedException {
        int pivot = arr[right];
        int i = (left - 1);
        for (int j = left; j < right; j++) {
            SortResponse response = new SortResponse(j, right);
            if (arr[j] <= pivot) {
                i++;
                response = new SortResponse(i, j, true);
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
            messagingTemplate.convertAndSend("/topic/quickSort", response);
            Thread.sleep(delay);
        }
        SortResponse response = new SortResponse(i+1, right, true);
        messagingTemplate.convertAndSend("/topic/quickSort", response);
        Thread.sleep(delay);
        int temp = arr[i + 1];
        arr[i + 1] = arr[right];
        arr[right] = temp;

        return i + 1;
    }

}
