package org.alejo2075.binary_search_service.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;

class SimpleMergeSortServiceTest {

    private SimpleMergeSortWorkerService service;

    @BeforeEach
    void setUp() {
        service = new SimpleMergeSortWorkerService();
    }

    @Test
    void testStartMergeSortProcess_EmptyArray() {
        int[] emptyArray = {};
        assertArrayEquals(emptyArray, service.startMergeSortProcess(emptyArray));
    }

    @Test
    void testStartMergeSortProcess_SingleElement() {
        int[] singleElementArray = {42};
        assertArrayEquals(singleElementArray, service.startMergeSortProcess(singleElementArray));
    }

    @Test
    void testStartMergeSortProcess_PreSortedArray() {
        int[] sortedArray = {1, 2, 3, 4, 5};
        assertArrayEquals(sortedArray, service.startMergeSortProcess(sortedArray.clone()));
    }

    @Test
    void testStartMergeSortProcess_ReverseSortedArray() {
        int[] reverseSortedArray = {5, 4, 3, 2, 1};
        int[] expectedArray = {1, 2, 3, 4, 5};
        assertArrayEquals(expectedArray, service.startMergeSortProcess(reverseSortedArray));
    }

    @Test
    void testStartMergeSortProcess_RandomArray() {
        int[] randomArray = {5, 2, 9, 1, 5, 6};
        int[] expectedArray = {1, 2, 5, 5, 6, 9};
        assertArrayEquals(expectedArray, service.startMergeSortProcess(randomArray));
    }

    @Test
    void testStartMergeSortProcess_AllElementsSame() {
        int[] sameElementsArray = {7, 7, 7, 7};
        assertArrayEquals(sameElementsArray, service.startMergeSortProcess(sameElementsArray));
    }

    @Test
    void testStartMergeSortProcess_LargeArray() {
        int size = 100000;
        Random random = new Random();
        int[] largeArray = random.ints(size, 1, 100000).toArray();
        int[] sortedArray = Arrays.copyOf(largeArray, largeArray.length);
        Arrays.sort(sortedArray);

        assertArrayEquals(sortedArray, service.startMergeSortProcess(largeArray));
    }
}
