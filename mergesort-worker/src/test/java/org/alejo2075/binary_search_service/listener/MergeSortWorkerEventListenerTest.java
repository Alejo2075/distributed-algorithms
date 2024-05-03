package org.alejo2075.binary_search_service.listener;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.alejo2075.binary_search_service.model.MergeSortRequest;
import org.alejo2075.binary_search_service.model.MergeSortResponse;
import org.alejo2075.binary_search_service.service.MergeSortWorkerService;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class MergeSortWorkerEventListenerTest {

    @Mock
    private MergeSortWorkerService mockWorkerService;

    @Mock
    private KafkaTemplate<String, String> mockKafkaTemplate;

    @InjectMocks
    private MergeSortWorkerEventListener listener;

    @Captor
    private ArgumentCaptor<String> kafkaMessageCaptor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // setUp code here, if necessary
    }

    @Test
    void processAndPublishSortedTask_ValidMessage() throws Exception {
        String jsonMessage = "{\"requestId\":\"req123\",\"taskId\":\"task123\",\"array\":[4, 1, 3, 2]}";
        when(mockWorkerService.startMergeSortProcess(any(int[].class))).thenReturn(new int[]{1, 2, 3, 4});

        listener.processAndPublishSortedTask(jsonMessage);

        verify(mockKafkaTemplate).send(eq("mergesort-tasks-processed"), eq("req123"), kafkaMessageCaptor.capture());
        MergeSortResponse response = objectMapper.readValue(kafkaMessageCaptor.getValue(), MergeSortResponse.class);
        assertArrayEquals(new int[]{1, 2, 3, 4}, response.getSortedArray());
    }

    @Test
    void processAndPublishSortedTask_InvalidJsonMessage() throws JsonProcessingException {
        String invalidJsonMessage = "this is not a json";
        listener.processAndPublishSortedTask(invalidJsonMessage);

        verify(mockKafkaTemplate, never()).send(anyString(), anyString(), anyString());
        verify(mockWorkerService, never()).startMergeSortProcess(any());
    }

    @Test
    void processAndPublishSortedTask_EmptyArray() throws Exception {
        String jsonMessage = "{\"requestId\":\"req123\",\"taskId\":\"task123\",\"array\":[]}";
        when(mockWorkerService.startMergeSortProcess(any(int[].class))).thenReturn(new int[]{});

        listener.processAndPublishSortedTask(jsonMessage);

        verify(mockKafkaTemplate).send(eq("mergesort-tasks-processed"), eq("req123"), kafkaMessageCaptor.capture());
        MergeSortResponse response = objectMapper.readValue(kafkaMessageCaptor.getValue(), MergeSortResponse.class);
        assertArrayEquals(new int[]{}, response.getSortedArray());
    }

    @Test
    void processAndPublishSortedTask_LargeArrayHandling() throws Exception {
        int[] largeArray = new int[1000];  // Simulate a large array of increasing numbers
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = i;
        }
        String jsonMessage = objectMapper.writeValueAsString(new MergeSortRequest("req123", "task123", largeArray));

        when(mockWorkerService.startMergeSortProcess(any(int[].class))).thenReturn(largeArray);
        listener.processAndPublishSortedTask(jsonMessage);

        verify(mockKafkaTemplate).send(eq("mergesort-tasks-processed"), eq("req123"), kafkaMessageCaptor.capture());
        MergeSortResponse response = objectMapper.readValue(kafkaMessageCaptor.getValue(), MergeSortResponse.class);
        assertArrayEquals(largeArray, response.getSortedArray());
    }
}
