package org.alejo2075.binary_search_service.service;

import org.springframework.stereotype.Service;


@Service
public class SimpleMergeSortWorkerService implements MergeSortWorkerService {


    @Override
    public int[] startMergeSortProcess(int[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }
        mergeSort(array, 0, array.length - 1);
        return array;
    }


    private void mergeSort(int[] array, int begin, int end) {
        if (begin < end) {
            int mid = (begin + end) / 2;
            mergeSort(array, begin, mid);
            mergeSort(array, mid + 1, end);
            merge(array, begin, mid, end);
        }
    }


    private void merge(int[] array, int begin, int mid, int end) {
        int[] temp = new int[end - begin + 1];
        int i = begin, j = mid + 1, k = 0;

        // Merge the two sorted subarrays into a temporary array.
        while (i <= mid && j <= end) {
            temp[k++] = array[i] <= array[j] ? array[i++] : array[j++];
        }

        // Copy the remaining elements of the left subarray, if any.
        while (i <= mid) {
            temp[k++] = array[i++];
        }

        // Copy the remaining elements of the right subarray, if any.
        while (j <= end) {
            temp[k++] = array[j++];
        }

        // Copy the sorted elements back into the original array.
        System.arraycopy(temp, 0, array, begin, temp.length);
    }
}
