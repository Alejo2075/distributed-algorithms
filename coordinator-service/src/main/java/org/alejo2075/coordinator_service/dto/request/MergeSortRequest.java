package org.alejo2075.coordinator_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeSortRequest {

    @NotNull(message = "The array must not be null.")
    @Size(min = 1, message = "The array must contain at least one element.")
    private int[] arr;
}
