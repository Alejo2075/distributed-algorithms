package org.alejo2075.coordinator_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeSortResult {

    private String requestId;
    private int[] sortedArray;
}
