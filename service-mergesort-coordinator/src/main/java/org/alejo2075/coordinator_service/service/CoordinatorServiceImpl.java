package org.alejo2075.coordinator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class CoordinatorServiceImpl implements CoordinatorService{

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String KAFKA_TOPIC = "mergesort-tasks-to-process";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CoordinatorServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void startMergeSortProcess(String requestId, int[] array) throws JsonProcessingException {

    }
}
