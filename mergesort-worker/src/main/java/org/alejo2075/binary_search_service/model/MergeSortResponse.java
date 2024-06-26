package org.alejo2075.binary_search_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeSortResponse implements Serializable {
    private String requestId;
    private String taskId;
    private int[] sortedArray;
}
