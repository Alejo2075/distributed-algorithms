package org.alejo2075.coordinator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.MergeSortCounter;
import org.alejo2075.coordinator_service.model.MergeSortTaskToProcess;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class MergeSortCoordinatorService{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String KAFKA_TOPIC = "mergesort-tasks-to-process";
    private static final int PARTITION_NUMBER = 2;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HashOperations<String, String, Object> hashOps;


    public MergeSortCoordinatorService(KafkaTemplate<String, String> kafkaTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }



    public void startMergeSortProcess(int[] array) throws JsonProcessingException {
        Instant startTime = Instant.now();
        log.info("Starting merge sort process at: {}", startTime);
        hashOps.put("MergeSortStartTime", "startTime", startTime.toString());

        List<int[]> segments = splitArray(array, PARTITION_NUMBER);
        MergeSortCounter counter = new MergeSortCounter(0, new String[segments.size()]);
        log.info("Array has been split into {} parts.", segments.size());

        for (int[] segment : segments) {
            String taskId = UUID.randomUUID().toString();
            MergeSortTaskToProcess task = new MergeSortTaskToProcess(taskId, segment);
            String message = objectMapper.writeValueAsString(task);
            log.info("Message: {}", message);
            kafkaTemplate.send(KAFKA_TOPIC, message);
            log.info("Published task {} to Kafka topic {}", taskId, KAFKA_TOPIC);

            counter.getTaskIds()[counter.getCounter()] = taskId;
            counter.setCounter(counter.getCounter() + 1);
            hashOps.put("MergeSortTasks", taskId, task.getArraySegment());
            log.info("Saved task {} to Redis", taskId);
        }

        hashOps.put("MergeSortCounter", "0", objectMapper.writeValueAsString(counter));
        log.info("Counter has been updated and saved to Redis");
    }


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
