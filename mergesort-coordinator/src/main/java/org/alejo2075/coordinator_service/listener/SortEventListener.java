package org.alejo2075.coordinator_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.MergeSortCounter;
import org.alejo2075.coordinator_service.model.MergeSortTaskProcessed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

/**
 * Listener component for handling sorted segments of arrays processed by workers.
 * This component listens to a Kafka topic for sorted array segments and manages the sorting process
 * by updating tasks, checking completion, and combining sorted segments.
 */
@Component
@Log4j2
public class SortEventListener {

    private static final String KAFKA_TOPIC = "mergesort-tasks-processed";
    private final ObjectMapper objectMapper;
    private final HashOperations<String, String, Object> hashOps;

    /**
     * Constructor for SortEventListener which initializes dependencies needed for Redis operations and JSON processing.
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
     * @param message The JSON message received from Kafka.
     * @throws JsonProcessingException if the message cannot be processed.
     */
    @KafkaListener(topics = KAFKA_TOPIC, groupId = "sort-group")
    public void onSortEventReceived(String message) throws JsonProcessingException {
        try {
            MergeSortTaskProcessed processedTask = objectMapper.readValue(message, MergeSortTaskProcessed.class);
            log.info("Received sorted segment for task ID: {}", processedTask.getTaskId());

            updateTaskInRedis(processedTask);
            if (checkAndCombineTasksIfCompleted()) {
                calculateTime();
                cleanupTasks();
            }
        } catch (Exception e) {
            log.error("Error processing message from Kafka", e);
        }
    }

    /**
     * Updates the task information in Redis with the sorted data from a worker.
     * @param task the processed task containing the sorted segment of the array.
     */
    private void updateTaskInRedis(MergeSortTaskProcessed task) {
        hashOps.put("MergeSortTasks", task.getTaskId(), task);
        MergeSortCounter counter = (MergeSortCounter) hashOps.get("MergeSortCounters", "0");
        if (counter != null) {
            counter.setCounter(counter.getCounter() - 1);
            hashOps.put("MergeSortCounter", "0", counter);
            log.info("Updated counter remaining tasks: {}", counter.getCounter());
        }
    }

    /**
     * Checks if all tasks are completed and combines all sorted segments if they are.
     * @return true if all tasks are completed and combined, false otherwise.
     */
    private boolean checkAndCombineTasksIfCompleted() {
        MergeSortCounter counter = (MergeSortCounter) hashOps.get("MergeSortCounters", "0");
        if (counter != null && counter.getCounter() == 0) {
            log.info("All segments sorted");
            int[] sortedArray = combineAndSortAllSegments(counter);
            return true;
        }
        return false;
    }

    /**
     * Calculates the total time taken for the merge sort process from start to finish.
     * This method retrieves the start time from Redis and calculates the duration until the current time.
     */
    private void calculateTime() {
        Instant endTime = Instant.now();
        String startTimeStr = (String) hashOps.get("MergeSortStartTime", "startTime");
        if (startTimeStr != null) {
            Instant startTime = Instant.parse(startTimeStr);
            Duration duration = Duration.between(startTime, endTime);
            log.info("Array Sorted. Total time taken: {}", duration);
        } else {
            log.error("Start time not found in Redis");
        }
    }

    /**
     * Combines and sorts all segments stored in Redis once all tasks are completed.
     * This method retrieves each task's sorted segment, combines them, and sorts the final array.
     * @param counter The counter containing IDs of all tasks.
     * @return A sorted array combining all segments.
     */
    protected int[] combineAndSortAllSegments(MergeSortCounter counter) {
        return Arrays.stream(counter.getTaskIds())
                .map(taskId -> (MergeSortTaskProcessed) hashOps.get("MergeSortTasks", taskId))
                .filter(Objects::nonNull)
                .flatMapToInt(task -> Arrays.stream(task.getArraySortedSegment()))
                .sorted()
                .toArray();
    }

    /**
     * Cleans up all tasks and counters from Redis after the sorting process is complete.
     */
    private void cleanupTasks() {
        MergeSortCounter counter = (MergeSortCounter) hashOps.get("MergeSortCounters", "0");
        Arrays.stream(counter.getTaskIds()).forEach(taskId -> hashOps.delete("MergeSortTasks", taskId));
        hashOps.delete("MergeSortCounter", "0");
        log.info("Cleaned up all tasks and counter");
    }
}
