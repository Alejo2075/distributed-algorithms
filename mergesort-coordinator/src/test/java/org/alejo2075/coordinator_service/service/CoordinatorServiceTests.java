package org.alejo2075.coordinator_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

/**
 * Unit tests for CoordinatorService.
 */
class CoordinatorServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private HashOperations<String, String, Object> hashOperations;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MergeSortCoordinatorServiceImpl mergeSortCoordinatorServiceImpl;

}
