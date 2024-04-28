package org.alejo2075.binary_search_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.binary_search_service.model.MergeSortRequest;
import org.alejo2075.binary_search_service.model.MergeSortResponse;
import org.alejo2075.binary_search_service.service.MergeSortService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Listener for Kafka events related to merge sort tasks.
 * Consumes messages from a specified Kafka topic, processes them using the merge sort service,
 * and publishes the results to another Kafka topic.
 */
@Component
@Log4j2
public class SortEventListener {

    private final MergeSortService mergeSortService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String RESULT_TOPIC = "mergesort-tasks-processed";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructs a SortEventListener with necessary services and utilities.
     *
     * @param mergeSortService the service to handle the merge sort logic.
     * @param kafkaTemplate the Kafka template for publishing messages.
     */
    public SortEventListener(MergeSortService mergeSortService, KafkaTemplate<String, String> kafkaTemplate) {
        this.mergeSortService = mergeSortService;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Handles the incoming sort tasks from Kafka, processes them, and publishes the results.
     *
     * @param message the JSON message representing a sort task.
     * @throws JsonProcessingException if an error occurs in processing JSON data.
     */
    @KafkaListener(topics = "mergeSortTasks", groupId = "merge-sort-group")
    public void processAndPublishSortedTask(String message) throws JsonProcessingException {
        try {
            MergeSortRequest request = deserializeMessage(message);
            int[] sortedArray = mergeSortService.startMergeSortProcess(request.getArray());
            log.info("Sorted array for Task ID: {} and Request ID: {}", request.getTaskId(), request.getRequestId());

            publishSortedArray(request, sortedArray);
        } catch (JsonProcessingException e) {
            log.error("Error processing JSON message: {}", message, e);
        } catch (Exception e) {
            log.error("Unexpected error processing message: {}", message, e);
        }
    }

    private MergeSortRequest deserializeMessage(String message) throws JsonProcessingException {
        return objectMapper.readValue(message, MergeSortRequest.class);
    }

    private void publishSortedArray(MergeSortRequest request, int[] sortedArray) throws JsonProcessingException {
        MergeSortResponse response = new MergeSortResponse(request.getRequestId(), request.getTaskId(), sortedArray);
        String jsonResponse = objectMapper.writeValueAsString(response);
        kafkaTemplate.send(RESULT_TOPIC, request.getRequestId(), jsonResponse);
        log.info("Published sorted array for Task ID: {} to topic {}", request.getTaskId(), RESULT_TOPIC);
    }
}
