package org.alejo2075.mergesort_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alejo2075.mergesort_service.model.MergeSortRequest;
import org.alejo2075.mergesort_service.model.MergeSortResponse;
import org.alejo2075.mergesort_service.service.MergeSortService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SortEventListenerTest {

    @Mock
    private MergeSortService mergeSortService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private SortEventListener listener;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSuccessfulSortAndPublish() throws Exception {
        int[] array = {5, 3, 8, 6, 2};
        int[] sortedArray = {2, 3, 5, 6, 8};
        MergeSortRequest mergeSortRequest = new MergeSortRequest("requestId123", "taskId456", array);
        String jsonMessage = objectMapper.writeValueAsString(mergeSortRequest);

        when(mergeSortService.startMergeSortProcess(array)).thenReturn(sortedArray);

        listener.processAndPublishSortedTask(jsonMessage);

        MergeSortResponse response = new MergeSortResponse("requestId123", "taskId456", sortedArray);
        String jsonResponse = objectMapper.writeValueAsString(response);

        verify(kafkaTemplate).send("mergeSortResults", "requestId123", jsonResponse);
    }

    @Test
    public void testMalformedJsonMessage() throws JsonProcessingException {
        String badJsonMessage = "This is not a valid JSON!";

        listener.processAndPublishSortedTask(badJsonMessage);

        verify(mergeSortService, never()).startMergeSortProcess(any());
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    public void testLargeArraySortPerformance() throws Exception {
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = (int) (Math.random() * 10000);
        }
        MergeSortRequest mergeSortRequest = new MergeSortRequest("requestId789", "taskId012", largeArray);
        String jsonMessage = objectMapper.writeValueAsString(mergeSortRequest);

        when(mergeSortService.startMergeSortProcess(largeArray)).thenReturn(largeArray); // Assuming it sorts in place for simplicity

        listener.processAndPublishSortedTask(jsonMessage);

        MergeSortResponse response = new MergeSortResponse("requestId789", "taskId012", largeArray);
        String jsonResponse = objectMapper.writeValueAsString(response);

        verify(kafkaTemplate).send("mergeSortResults", "requestId789", jsonResponse);
    }



}
