

package org.alejo2075.mergesort_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MergeSortServiceImplTest {

    @InjectMocks
    private MergeSortServiceImpl mergeSortService;
    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        mergeSortService = new MergeSortServiceImpl();
    }

    @Test
    void testSortEmptyArray() {
        int[] actual = {};
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(new int[] {}, actual);
    }

    @Test
    void testSortSingleElement() {
        int[] actual = {1};
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(new int[] {1}, actual);
    }

    @Test
    void testSortMultipleElements() {
        int[] actual = {5, 3, 8, 6, 2};
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(new int[] {2, 3, 5, 6, 8}, actual);
    }

    @Test
    void testSortWithNegativeNumbers() {
        int[] actual = {-3, -1, -4, -2};
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(new int[] {-4, -3, -2, -1}, actual);
    }

    @Test
    void testSortWithDuplicates() {
        int[] actual = {5, 3, 5, 5, 2};
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(new int[] {2, 3, 5, 5, 5}, actual);
    }

    @Test
    void testSortAlreadySortedArray() {
        int[] actual = {1, 2, 3, 4, 5};
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, actual);
    }

    @Test
    void testSortReverseSortedArray() {
        int[] actual = {5, 4, 3, 2, 1};
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, actual);
    }

    @Test
    void testSortLargeArray() {
        int[] actual = new int[1000];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = random.nextInt(1000); // Random numbers between 0 and 999
        }
        int[] expected = actual.clone();
        java.util.Arrays.sort(expected); // Sorting using Java's built-in sort for comparison
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSortLargeArraySorted() {
        int[] actual = new int[1000];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = i;
        }
        int[] expected = actual.clone();
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSortLargeArrayReverse() {
        int[] actual = new int[1000];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = actual.length - i;
        }
        int[] expected = new int[1000];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = i + 1;
        }
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    void testSortVeryLargeArray() {
        int[] actual = new int[10000];
        for (int i = 0; i < actual.length; i++) {
            actual[i] = random.nextInt(10000); // Random numbers between 0 and 9999
        }
        int[] expected = actual.clone();
        java.util.Arrays.sort(expected); // Sorting using Java's built-in sort for comparison
        mergeSortService.startMergeSortProcess(actual);
        assertArrayEquals(expected, actual);
    }
}

