package com.alejo2075.webflow.algorithms;

import com.alejo2075.webflow.dtos.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SortAlgorithms {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void bubbleSort(int[] arr, int delay) throws InterruptedException {
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

    public void selectionSort(int[] arr, int delay) throws InterruptedException {
        for (int i = 0; i < arr.length - 1; i++) {
            SortResponse response = new SortResponse();
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                response = new SortResponse(minIndex, j);
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
                messagingTemplate.convertAndSend("/topic/selectionSort", response);
                Thread.sleep(delay);
            }
            response = new SortResponse(minIndex, i, true);
            messagingTemplate.convertAndSend("/topic/selectionSort", response);
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
        messagingTemplate.convertAndSend("/topic/selectionSort", arr);
    }

    public void insertionSort(int[] arr, int delay) throws InterruptedException {
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

    public void mergeSort(int[] arr, int delay) throws InterruptedException {
        mergeSortHelper(arr, 0, arr.length - 1, delay);
        messagingTemplate.convertAndSend("/topic/mergeSort", arr);
    }

    private void mergeSortHelper(int[] arr, int left, int right, int delay) throws InterruptedException {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortHelper(arr, left, mid, delay);
            mergeSortHelper(arr, mid + 1, right, delay);
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
                response.setFromIndex(i + left);
                response.setToIndex(k);
                response.setAreChange(true);
                messagingTemplate.convertAndSend("/topic/mergeSort", response);
                arr[k] = L[i];
                i++;
            } else {
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

    public void quickSort(int[] arr, int delay) throws InterruptedException {
        quickSortHelper(arr, 0, arr.length - 1, delay);
        messagingTemplate.convertAndSend("/topic/quickSort", arr);
    }

    private void quickSortHelper(int[] arr, int left, int right, int delay) throws InterruptedException {
        if(left < right) {
            int index = partition(arr, left, right, delay);
            quickSortHelper(arr, left, index - 1, delay);
            quickSortHelper(arr, index + 1, right, delay);
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
