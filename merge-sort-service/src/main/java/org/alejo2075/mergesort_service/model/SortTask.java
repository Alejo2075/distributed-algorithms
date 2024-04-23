package org.alejo2075.mergesort_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortTask implements Serializable {
    private String requestId;
    private String taskId;
    private int[] array;
}
