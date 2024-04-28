package org.alejo2075.coordinator_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("MergeSortTask")
public class MergeSortTask {

    private String requestId;
    private String taskId;
    private int[] segment;
}
