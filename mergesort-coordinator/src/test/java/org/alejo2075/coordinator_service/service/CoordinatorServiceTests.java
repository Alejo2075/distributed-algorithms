package org.alejo2075.coordinator_service.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.HashOperations;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CoordinatorServiceTests {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, String, Object> hashOperations;

    @InjectMocks
    private CoordinatorService coordinatorService;



    @Test
    public void testStartMergeSortProcessPublishesTasks() throws JsonProcessingException {
        int[] array = {3, 2, 1};
        String requestId = "req123";

        //when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        coordinatorService.startMergeSortProcess(requestId, array);

        verify(kafkaTemplate, times(2)).send(eq("mergesort-tasks-to-process"), anyString());
        verify(hashOperations, times(3)).put(anyString(), anyString(), any());
    }

    @Test
    public void testStartMergeSortProcessHandlesEmptyArray() {
        int[] array = {};
        String requestId = "req123";

        //when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            coordinatorService.startMergeSortProcess(requestId, array);
        });

        assertEquals("The array must not be null or empty.", exception.getMessage());
    }

    @Test
    public void testStartMergeSortProcessHandlesNullArray() {
        String requestId = "req123";

        //when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            coordinatorService.startMergeSortProcess(requestId, null);
        });

        assertEquals("The array must not be null or empty.", exception.getMessage());
    }

    @Test
    public void testSplitArrayCorrectlySplitsArrayIntoTwo() {
        int[] array = {5, 4, 3, 2, 1};
        List<int[]> segments = coordinatorService.splitArray(array, 2);

        assertEquals(2, segments.size());
        assertArrayEquals(new int[]{5, 4, 3}, segments.get(0));
        assertArrayEquals(new int[]{2, 1}, segments.get(1));
    }

    @Test
    public void testSplitArrayHandlesUnevenSplits() {
        int[] array = {5, 4, 3, 2, 1, 0};
        List<int[]> segments = coordinatorService.splitArray(array, 3);

        assertEquals(3, segments.size());
        assertArrayEquals(new int[]{5, 4}, segments.get(0));
        assertArrayEquals(new int[]{3, 2}, segments.get(1));
        assertArrayEquals(new int[]{1, 0}, segments.get(2));
    }
}
