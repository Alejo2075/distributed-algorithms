package com.alejo2075.webflow.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortResponse {
    private int fromIndex;
    private int toIndex;
    private boolean areChange;

    public SortResponse(int fromIndex, int toIndex){
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        areChange = false;
    }

}
