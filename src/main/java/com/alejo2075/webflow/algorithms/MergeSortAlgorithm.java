package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MergeSortAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sort(int[] arr, int delay){
        mergeSort(arr, 0, arr.length - 1, delay);
        messagingTemplate.convertAndSend("/topic/mergeSort", arr);
    }

    private void mergeSort(int[] arr, int left, int right, int delay) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid, delay);
            mergeSort(arr, mid + 1, right, delay);
            merge(arr, left, mid, right, delay);
        }
    }

    private void merge(int[] arr, int left, int mid, int right, int delay) throws InterruptedException {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int L[] = new int[n1];
        int R[] = new int[n2];

        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, mid + 1, R, 0, n2);

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            SortResponse response = new SortResponse();
            if (L[i] <= R[j]) {
                //response = new SortResponse(i + left, k , true);
                response.setFromIndex(i + left);
                response.setToIndex(k);
                response.setAreChange(true);
                messagingTemplate.convertAndSend("/topic/mergeSort", response);
                arr[k] = L[i];
                i++;
            } else {
                //response = new SortResponse(j + mid + 1, k , true);
                response.setFromIndex(j + mid + 1);
                response.setToIndex(k);
                response.setAreChange(true);
                messagingTemplate.convertAndSend("/topic/mergeSort", response);
                arr[k] = R[j];
                j++;
            }
            Thread.sleep(delay);
            k++;
        }
        while (i < n1) {
            SortResponse response = new SortResponse(i + left, k, true);
            messagingTemplate.convertAndSend("/topic/mergeSort", response);
            arr[k] = L[i];
            i++;
            k++;
            Thread.sleep(delay);
        }
        while (j < n2) {
            SortResponse response = new SortResponse(j + mid + 1, k, true);
            messagingTemplate.convertAndSend("/topic/mergeSort", response);
            arr[k] = R[j];
            j++;
            k++;
            Thread.sleep(delay);
        }
    }
}
