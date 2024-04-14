package org.alejo2075.coordinator_service.listener;

import org.alejo2075.coordinator_service.event.SortEvent;
import org.alejo2075.coordinator_service.util.JsonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SortEventListener {

    @KafkaListener(topics = "sorted-topic")
    public void handleOrdersNotifications(String message) {
        var orderEvent = JsonUtil.fromJson(message, SortEvent.class);

    }
}
