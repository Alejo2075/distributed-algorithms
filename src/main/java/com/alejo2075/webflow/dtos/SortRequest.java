package com.alejo2075.webflow.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {
    private int[] arr;
    private int delay;
}
