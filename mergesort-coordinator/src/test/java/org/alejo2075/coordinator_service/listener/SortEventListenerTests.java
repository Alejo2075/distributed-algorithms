package org.alejo2075.coordinator_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Unit tests for SortEventListener.
 */
class SortEventListenerTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HashOperations<String, String, Object> hashOperations;

    @InjectMocks
    private MergeSortCoordinatorEventListener mergeSortCoordinatorEventListener;


}
