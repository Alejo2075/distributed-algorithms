package org.alejo2075.coordinator_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SortEventListener {

    private static final Logger logger = LogManager.getLogger(SortEventListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*@KafkaListener(topics = "mergesort-tasks-processed", groupId = "sort-group")
    public void onSortEventReceived(String message) {
        try {
            SortEvent sortEvent = objectMapper.readValue(message, SortEvent.class);
            logger.info("Received sorted array: {} elements long.", sortEvent.sortedArr().length);
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize sort event message", e);
        } catch (Exception e) {
            logger.error("Error processing sorted event", e);
        }
    }*/
}
