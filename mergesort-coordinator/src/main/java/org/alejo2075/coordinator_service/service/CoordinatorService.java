package org.alejo2075.coordinator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.MergeSortCounter;
import org.alejo2075.coordinator_service.model.MergeSortTaskToProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service class to handle the coordination of MergeSort tasks.
 * This service is responsible for initiating and managing the lifecycle of merge sort operations,
 * including distributing tasks to Kafka and storing task and state information in Redis.
 */
@Service
@Log4j2
public class CoordinatorService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String KAFKA_TOPIC = "mergesort-tasks-to-process";
    private static final int PARTITION_NUMBER = 2;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HashOperations<String, String, Object> hashOps;

    /**
     * Constructs a new CoordinatorService with required components.
     * @param kafkaTemplate the Kafka template for publishing messages.
     * @param redisTemplate the Redis template for managing Redis operations.
     */
    @Autowired
    public CoordinatorService(KafkaTemplate<String, String> kafkaTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }

    /**
     * Starts the merge sort process for a given array.
     * Validates the input array, initializes the sorting process, splits the array into segments,
     * and handles the coordination of task distribution and state management.
     * @param array the array to sort.
     * @throws JsonProcessingException if an error occurs during object mapping.
     */
    public void startMergeSortProcess(int[] array) throws JsonProcessingException {
        if (!isValidInput(array)) {
            return;
        }

        Instant startTime = Instant.now();
        initiateTiming(startTime);
        List<int[]> segments = splitArray(array, PARTITION_NUMBER);

        if (segments.isEmpty()) {
            log.error("Array could not be split into segments");
            return;
        }

        MergeSortCounter counter = initializeCounter(segments.size());
        processSegments(segments, counter);
        finalizeCounter(counter);
    }

    /**
     * Validates the input array to ensure it is not null or empty.
     * @param array the array to validate.
     * @return true if the array is valid, false otherwise.
     */
    private boolean isValidInput(int[] array) {
        if (array == null || array.length == 0) {
            log.error("Input array is null or empty");
            return false;
        }
        return true;
    }

    /**
     * Records the start time of the sorting process in Redis.
     * @param startTime the starting instant of the process.
     */
    private void initiateTiming(Instant startTime) {
        log.info("Starting merge sort process at: {}", startTime);
        hashOps.put("MergeSortStartTime", "startTime", startTime.toString());
    }

    /**
     * Initializes a new MergeSortCounter with the size based on the number of segments.
     * @param segmentSize the number of segments into which the array is split.
     * @return a new MergeSortCounter instance.
     */
    private MergeSortCounter initializeCounter(int segmentSize) {
        return new MergeSortCounter(0, new String[segmentSize]);
    }

    /**
     * Processes each segment of the array by publishing it to Kafka and storing related state in Redis.
     * @param segments the list of array segments to process.
     * @param counter the counter tracking the number of tasks.
     * @throws JsonProcessingException if an error occurs during object mapping.
     */
    private void processSegments(List<int[]> segments, MergeSortCounter counter) throws JsonProcessingException {
        for (int[] segment : segments) {
            String taskId = UUID.randomUUID().toString();
            MergeSortTaskToProcess task = new MergeSortTaskToProcess(taskId, segment);
            String message = objectMapper.writeValueAsString(task);
            kafkaTemplate.send(KAFKA_TOPIC, message);
            log.info("Published task {} to Kafka topic {}", taskId, KAFKA_TOPIC);
            storeTask(taskId, segment, counter);
        }
    }

    /**
     * Stores task information in Redis and updates the task counter.
     * @param taskId the ID of the task.
     * @param segment the segment of the array associated with the task.
     * @param counter the counter tracking the number of tasks.
     */
    private void storeTask(String taskId, int[] segment, MergeSortCounter counter) {
        hashOps.put("MergeSortTasks", taskId, segment);
        counter.getTaskIds()[counter.getCounter()] = taskId;
        counter.setCounter(counter.getCounter() + 1);
        log.info("Saved task {} to Redis", taskId);
    }

    /**
     * Finalizes the counter by updating its state in Redis after all tasks have been processed.
     * @param counter the counter to finalize.
     */
    private void finalizeCounter(MergeSortCounter counter) {
        hashOps.put("MergeSortCounter", "0", counter);
        log.info("Counter has been updated and saved to Redis");
    }

    /**
     * Splits an array into a specified number of parts.
     * This method handles the division of the array into nearly equal parts, accounting for remainders.
     * @param array the array to split.
     * @param parts the number of parts to split the array into.
     * @return a list of array segments.
     */
    protected List<int[]> splitArray(int[] array, int parts) {
        if (parts <= 0 || array.length < parts) {
            log.error("Invalid number of parts: {} for array of length: {}", parts, array.length);
            return new ArrayList<>();
        }

        int length = array.length;
        int n = length / parts;
        int r = length % parts;
        List<int[]> segments = new ArrayList<>();
        for (int i = 0; i < parts; i++) {
            int start = i * n + Math.min(i, r);
            int end = start + n + (i < r ? 1 : 0);
            int[] segment = new int[end - start];
            System.arraycopy(array, start, segment, 0, segment.length);
            segments.add(segment);
        }
        return segments;
    }
}
