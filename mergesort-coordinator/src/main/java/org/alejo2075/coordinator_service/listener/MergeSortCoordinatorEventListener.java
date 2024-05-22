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

@Component
@Log4j2
public class MergeSortCoordinatorEventListener {

    private static final String KAFKA_TOPIC = "mergesort-tasks-processed";
    private static final String GROUP_ID = "sort-group";
    private final HashOperations<String, String, Object> hashOps;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public MergeSortCoordinatorEventListener(RedisTemplate<String, Object> redisTemplate) {
        this.hashOps = redisTemplate.opsForHash();
    }


    @KafkaListener(topics = KAFKA_TOPIC, groupId = GROUP_ID)
    public void onMergeSortEventReceived(String message) throws JsonProcessingException {
        MergeSortTaskProcessed processedTask = objectMapper.readValue(message, MergeSortTaskProcessed.class);
        log.info("Received sorted segment for task ID: {}", processedTask.getTaskId());

        updateTaskInRedis(processedTask);

        if (checkAndFinalizeSorting(processedTask.getTaskId())) {
            calculateAndLogSortingDuration();
            cleanupRedisEntries();
        }
    }


    private void updateTaskInRedis(MergeSortTaskProcessed task) {
        hashOps.put("MergeSortTasks", task.getTaskId(), task);
        MergeSortCounter counter = (MergeSortCounter) hashOps.get("MergeSortCounters", "0");
        assert counter != null;
        counter.setCounter(counter.getCounter() - 1);
        hashOps.put("MergeSortCounter", "0", counter);
    }


    private boolean checkAndFinalizeSorting(String taskId) {
        MergeSortCounter counter = (MergeSortCounter) hashOps.get("MergeSortCounter", "0");
        assert counter != null;
        if (counter.getCounter() == 0) {
            log.info("All segments sorted for task ID: {}", taskId);
            int[] sortedArray = combineAndSortAllSegments(counter);
            return true;
        }
        return false;
    }


    private void calculateAndLogSortingDuration() {
        String startTimeStr = (String) hashOps.get("MergeSortStartTime", "startTime");
        if (startTimeStr != null) {
            Instant startTime = Instant.parse(startTimeStr);
            Duration duration = Duration.between(startTime, Instant.now());
            log.info("Total sorting time: {} seconds", duration.getSeconds());
        } else {
            log.error("Start time not found in Redis for sorting duration calculation.");
        }
    }


    private void cleanupRedisEntries() {
        MergeSortCounter counter = (MergeSortCounter) hashOps.get("MergeSortCounter", "0");
        assert counter != null;
        Arrays.stream(counter.getTaskIds()).forEach(taskId -> hashOps.delete("MergeSortTasks", taskId));
        hashOps.delete("MergeSortCounter", "0");
        log.info("Cleaned up Redis entries for completed sorting tasks.");
    }


    protected int[] combineAndSortAllSegments(MergeSortCounter counter) {
        return Arrays.stream(counter.getTaskIds())
                .map(taskId -> (MergeSortTaskProcessed) hashOps.get("MergeSortTasks", taskId))
                .filter(Objects::nonNull)
                .flatMapToInt(task -> Arrays.stream(task.getArraySortedSegment()))
                .sorted()
                .toArray();
    }
}
