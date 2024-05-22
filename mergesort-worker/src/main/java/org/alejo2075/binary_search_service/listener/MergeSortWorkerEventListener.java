package org.alejo2075.binary_search_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.binary_search_service.model.MergeSortRequest;
import org.alejo2075.binary_search_service.model.MergeSortResponse;
import org.alejo2075.binary_search_service.service.MergeSortWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class MergeSortWorkerEventListener {

    private static final String KAFKA_TOPIC2 = "mergesort-tasks-processed";
    private static final String KAFKA_TOPIC = "mergesort-tasks-to-process";
    private static final String GROUP_ID = "sort-group";
    private final MergeSortWorkerService workerService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public MergeSortWorkerEventListener(MergeSortWorkerService workerService, KafkaTemplate<String, String> kafkaTemplate) {
        this.workerService = workerService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = KAFKA_TOPIC, groupId = GROUP_ID)
    public void processAndPublishSortedTask(String message) throws JsonProcessingException {
        try {
            MergeSortRequest request = objectMapper.readValue(message, MergeSortRequest.class);
            int[] sortedArray = workerService.startMergeSortProcess(request.getArray());
            log.info("Sorted array for Task ID: {} and Request ID: {}", request.getTaskId(), request.getRequestId());

            publishSortedArray(request, sortedArray);
        } catch (Exception e) {
            log.error("Failed to process and publish sorted array", e);
        }
    }

    protected void publishSortedArray(MergeSortRequest request, int[] sortedArray) throws JsonProcessingException {
        MergeSortResponse response = new MergeSortResponse(request.getRequestId(), request.getTaskId(), sortedArray);
        String jsonResponse = objectMapper.writeValueAsString(response);
        kafkaTemplate.send(KAFKA_TOPIC2, request.getRequestId(), jsonResponse);
        log.info("Published sorted array for Task ID: {} to topic {}", request.getTaskId(), KAFKA_TOPIC);
    }
}
