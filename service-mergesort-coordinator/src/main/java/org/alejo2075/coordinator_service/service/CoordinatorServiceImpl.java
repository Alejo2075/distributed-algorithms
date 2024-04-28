package org.alejo2075.coordinator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.MergeSortCounter;
import org.alejo2075.coordinator_service.model.MergeSortTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
public class CoordinatorServiceImpl implements CoordinatorService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String KAFKA_TOPIC = "mergesort-tasks-to-process";
    private static final int PARTITION_NUMBER = 2;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HashOperations<String, String, Object> hashOps;

    /**
     * Constructs a new CoordinatorServiceImpl with required components.
     *
     * @param kafkaTemplate the Kafka template for publishing messages.
     * @param redisTemplate the Redis template for managing Redis operations.
     */
    @Autowired
    public CoordinatorServiceImpl(KafkaTemplate<String, String> kafkaTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }

    /**
     * Starts the merge sort process for a given request ID and array.
     * This method splits the array into segments, publishes each segment to Kafka,
     * and stores task and counter information in Redis.
     *
     * @param requestId the unique ID for this sorting request.
     * @param array the array to sort.
     * @throws JsonProcessingException if an error occurs during object mapping.
     */
    @Override
    public void startMergeSortProcess(String requestId, int[] array) throws JsonProcessingException {
        log.info("Starting merge sort process for request ID: {}", requestId);

        List<int[]> segments = splitArray(array, PARTITION_NUMBER);
        MergeSortCounter counter = new MergeSortCounter(0, requestId, new String[segments.size()]);
        log.debug("Array has been split into {} parts for request ID: {}", segments.size(), requestId);

        for (int[] segment : segments) {
            String taskId = UUID.randomUUID().toString();
            MergeSortTask task = new MergeSortTask(requestId, taskId, segment);
            String message = objectMapper.writeValueAsString(task);
            kafkaTemplate.send(KAFKA_TOPIC, message);
            log.info("Published task {} for request {} to Kafka topic {}", taskId, requestId, KAFKA_TOPIC);

            counter.getTaskIds()[counter.getCounter()] = taskId;
            counter.setCounter(counter.getCounter() + 1);
            hashOps.put("MergeSortTasks", taskId, task);
            log.info("Saved task {} for request {} to Redis", taskId, requestId);
        }

        hashOps.put("MergeSortCounters", requestId, counter);
        log.info("Counter for request ID {} has been updated and saved to Redis", requestId);
    }

    /**
     * Splits an array into a specified number of parts.
     *
     * @param array the array to split.
     * @param parts the number of parts to split the array into.
     * @return a list of array segments.
     */
    protected List<int[]> splitArray(int[] array, int parts) {
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
