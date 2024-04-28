package org.alejo2075.coordinator_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.MergeSortCounter;
import org.alejo2075.coordinator_service.model.MergeSortResult;
import org.alejo2075.coordinator_service.model.MergeSortTaskProcessed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * Listener component for handling sorted segments of arrays processed by workers.
 * This component listens to Kafka topic for sorted array segments and manages the sorting process
 * by updating tasks, checking completion, and combining sorted segments.
 */
@Component
@Log4j2
public class SortEventListener {

    private static final String KAFKA_TOPIC = "mergesort-tasks-processed";
    private final ObjectMapper objectMapper;
    private final HashOperations<String, String, Object> hashOps;

    /**
     * Constructor for SortEventListener.
     *
     * @param redisTemplate The Redis template used for hash operations.
     * @param objectMapper The Jackson object mapper for JSON processing.
     */
    @Autowired
    public SortEventListener(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.hashOps = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    /**
     * Kafka listener method that triggers when a sorted segment message is received.
     * This method updates Redis with the task's sorted data, manages counters for sorting completion,
     * and combines all segments upon completion of all tasks.
     *
     * @param message The JSON message received from Kafka.
     * @throws JsonProcessingException if the message cannot be processed.
     */
    @KafkaListener(topics = KAFKA_TOPIC, groupId = "sort-group")
    public void onSortEventReceived(String message) throws JsonProcessingException {
        MergeSortTaskProcessed processedTask = objectMapper.readValue(message, MergeSortTaskProcessed.class);
        log.info("Received sorted segment for request ID: {}, task ID: {}", processedTask.getRequestId(), processedTask.getTaskId());

        // Update the task in Redis with the sorted array
        hashOps.put("MergeSortTasks", processedTask.getTaskId(), processedTask);

        // Update the counter and check if all segments are sorted
        MergeSortCounter counter = (MergeSortCounter) hashOps.get("MergeSortCounters", processedTask.getRequestId());
        assert counter != null;
        counter.setCounter(counter.getCounter() - 1);
        hashOps.put("MergeSortCounters", processedTask.getRequestId(), counter);
        log.info("Updated counter for request ID: {}, remaining tasks: {}", processedTask.getRequestId(), counter.getCounter());

        // Check if all segments are sorted
        if (counter.getCounter() == 0) {
            log.info("All segments sorted for request ID: {}", processedTask.getRequestId());
            int[] sortedArray = combineAndSortAllSegments(counter, processedTask.getRequestId());

            // Store the sorted array in Redis
            MergeSortResult result = new MergeSortResult(processedTask.getRequestId(), sortedArray);
            hashOps.put("MergeSortResults", processedTask.getRequestId(), result);
            log.info("Sorted array stored in Redis for request ID: {}", processedTask.getRequestId());

            // Clean up tasks and counter from Redis
            Arrays.stream(counter.getTaskIds()).forEach(taskId -> hashOps.delete("MergeSortTasks", taskId));
            hashOps.delete("MergeSortCounters", processedTask.getRequestId());
            log.info("Cleaned up all tasks and counter for request ID: {}", processedTask.getRequestId());
        }
    }

    /**
     * Combines and sorts all segments stored in Redis once all tasks are completed.
     *
     * @param counter The counter containing IDs of all tasks.
     * @param requestId The request ID associated with these tasks.
     * @return A sorted array combining all segments.
     */
    protected int[] combineAndSortAllSegments(MergeSortCounter counter, String requestId) {
        return Arrays.stream(counter.getTaskIds())
                .map(taskId -> (MergeSortTaskProcessed) hashOps.get("MergeSortTasks", taskId)).filter(Objects::nonNull)
                .flatMapToInt(task -> Arrays.stream(task.getArraySortedSegment()))
                .sorted()
                .toArray();
    }
}
