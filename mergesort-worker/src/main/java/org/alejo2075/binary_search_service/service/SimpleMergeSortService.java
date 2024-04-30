package org.alejo2075.binary_search_service.service;

import org.springframework.stereotype.Service;

/**
 * Service implementation for handling simple merge sort operations.
 */
@Service
public class SimpleMergeSortService implements MergeSortService {

    /**
     * Starts the merge sort process on the given array.
     *
     * @param array the array to sort.
     * @return the sorted array.
     */
    @Override
    public int[] startMergeSortProcess(int[] array) {
        if (array.length <= 1) {
            return array;
        }
        mergeSort(array, 0, array.length - 1);
        return array;
    }

    /**
     * Private method to perform the recursive merge sort.
     *
     * @param array the array to sort.
     * @param begin the beginning index for the segment to sort.
     * @param end the ending index for the segment to sort.
     */
    private void mergeSort(int[] array, int begin, int end) {
        if (begin < end) {
            int mid = (begin + end) / 2;
            mergeSort(array, begin, mid);
            mergeSort(array, mid + 1, end);
            merge(array, begin, mid, end);
        }
    }

    /**
     * Private method to merge two halves of an array.
     *
     * @param array the array containing the two halves to merge.
     * @param begin the beginning index of the first half.
     * @param mid the end index of the first half and the beginning index of the second half.
     * @param end the end index of the second half.
     */
    private void merge(int[] array, int begin, int mid, int end) {
        int[] temp = new int[end - begin + 1];
        int i = begin, j = mid + 1, k = 0;

        while (i <= mid && j <= end) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }
        while (j <= end) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, begin, temp.length);
    }
}
