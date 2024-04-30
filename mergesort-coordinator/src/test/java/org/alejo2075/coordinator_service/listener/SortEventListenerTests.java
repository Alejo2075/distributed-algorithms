package org.alejo2075.coordinator_service.listener;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import org.alejo2075.coordinator_service.model.MergeSortCounter;
import org.alejo2075.coordinator_service.model.MergeSortTaskProcessed;
import org.alejo2075.coordinator_service.model.MergeSortResult;

import java.util.Arrays;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class SortEventListenerTests {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, String, Object> hashOperations;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SortEventListener listener;

    @Test
    public void testOnSortEventReceivedUpdatesRedis() throws JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        String taskId = UUID.randomUUID().toString();
        String jsonMessage = String.format("{\"requestId\":\"%s\", \"taskId\":\"%s\", \"arraySortedSegment\":[1,2,3]}", requestId, taskId);
        MergeSortTaskProcessed task = new MergeSortTaskProcessed(requestId, taskId, new int[]{1, 2, 3});
        MergeSortCounter counter = new MergeSortCounter(1, requestId, new String[]{taskId});

        when(objectMapper.readValue(jsonMessage, MergeSortTaskProcessed.class)).thenReturn(task);
        //when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("MergeSortCounters", requestId)).thenReturn(counter);

        listener.onSortEventReceived(jsonMessage);

        verify(hashOperations).put("MergeSortTasks", taskId, task);
        verify(hashOperations).put("MergeSortCounters", requestId, counter);
    }

    @Test
    public void testOnSortEventReceivedHandlesJsonProcessingException() {
        String jsonMessage = "invalid json";

        assertThrows(JsonProcessingException.class, () -> {
            listener.onSortEventReceived(jsonMessage);
        });
    }

    @Test
    public void testOnSortEventReceivedAllSegmentsSorted() throws JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        String taskId = UUID.randomUUID().toString();
        String jsonMessage = String.format("{\"requestId\":\"%s\", \"taskId\":\"%s\", \"arraySortedSegment\":[1,2,3]}", requestId, taskId);
        MergeSortTaskProcessed task = new MergeSortTaskProcessed(requestId, taskId, new int[]{1, 2, 3});
        MergeSortCounter counter = new MergeSortCounter(0, requestId, new String[]{taskId}); // Simulate last task

        when(objectMapper.readValue(jsonMessage, MergeSortTaskProcessed.class)).thenReturn(task);
        //when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get("MergeSortCounters", requestId)).thenReturn(counter);

        listener.onSortEventReceived(jsonMessage);

        verify(hashOperations).put("MergeSortResults", requestId, new MergeSortResult(requestId, new int[]{1, 2, 3}));
        verify(hashOperations).delete("MergeSortTasks", taskId);
        verify(hashOperations).delete("MergeSortCounters", requestId);
    }

    @Test
    public void testCombineAndSortAllSegments() {
        String requestId = UUID.randomUUID().toString();
        String[] taskIds = {UUID.randomUUID().toString(), UUID.randomUUID().toString()};
        MergeSortTaskProcessed task1 = new MergeSortTaskProcessed(requestId, taskIds[0], new int[]{5, 3});
        MergeSortTaskProcessed task2 = new MergeSortTaskProcessed(requestId, taskIds[1], new int[]{4, 2, 1});
        MergeSortCounter counter = new MergeSortCounter(0, requestId, taskIds);

        when(hashOperations.get("MergeSortTasks", taskIds[0])).thenReturn(task1);
        when(hashOperations.get("MergeSortTasks", taskIds[1])).thenReturn(task2);

        int[] result = listener.combineAndSortAllSegments(counter, requestId);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }
}
