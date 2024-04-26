package org.alejo2075.coordinator_service.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alejo2075.coordinator_service.validation.Sorted;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BinarySearchRequest {

    @NotNull(message = "The array must not be null.")
    @Size(min = 1, message = "The array must contain at least one element.")
    @Sorted
    private int[] sortedArray;

    @NotNull(message = "The target must not be null.")
    private int target;

}
