package org.alejo2075.mergesort_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.mergesort_service.model.SortTask;
import org.alejo2075.mergesort_service.model.SortTaskCompleted;
import org.alejo2075.mergesort_service.service.MergeSortService;
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
    private static final String RESULT_TOPIC = "mergeSortResults";
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
    public void onSortEventReceived(String message) throws JsonProcessingException {
        try {
            SortTask sortTask = objectMapper.readValue(message, SortTask.class);
            int[] sortedArray = mergeSortService.startMergeSortProcess(sortTask.getArray());
            log.info("Sorted array for Task ID: {} and Request ID: {}", sortTask.getTaskId(), sortTask.getRequestId());

            SortTaskCompleted sortTaskCompleted = new SortTaskCompleted(sortTask.getRequestId(), sortTask.getTaskId(), sortedArray);
            String jsonResponse = objectMapper.writeValueAsString(sortTaskCompleted);
            kafkaTemplate.send(RESULT_TOPIC, sortTask.getRequestId(), jsonResponse);
            log.info("Published sorted array for Task ID: {} to topic {}", sortTask.getTaskId(), RESULT_TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Error processing or publishing message: {}", message, e);
        } catch (Exception e) {
            log.error("Unexpected error processing message: {}", message, e);
        }
    }
}
