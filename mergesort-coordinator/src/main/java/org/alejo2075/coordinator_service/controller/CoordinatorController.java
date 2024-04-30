package org.alejo2075.coordinator_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.dto.request.MergeSortRequest;
import org.alejo2075.coordinator_service.model.dto.response.MergeSortResponse;
import org.alejo2075.coordinator_service.service.CoordinatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller class that handles HTTP requests for sorting algorithms.
 * This controller is responsible for managing requests to perform sorting operations,
 * specifically MergeSort in this case, and delegating the processing to the {@link CoordinatorService}.
 */
@RestController
@RequestMapping("/v1/algorithms")
@Log4j2
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    /**
     * Constructs a new CoordinatorController with a dependency on CoordinatorService.
     *
     * @param coordinatorService The service that will handle the actual sorting process.
     */
    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    /**
     * Endpoint to initiate a merge sort on an integer array.
     * This method takes a JSON request containing an integer array and initiates a merge sort process.
     * It returns a response indicating the status of the process along with a unique request ID.
     *
     * @param request The MergeSortRequest containing the array to be sorted.
     * @return ResponseEntity containing MergeSortResponse with status and request ID.
     * @throws JsonProcessingException If an error occurs while processing the request data.
     *
     * @apiNote The endpoint validates the incoming request to ensure the array is not null and contains at least one element.
     * @apiResponse 200 Successful processing - the request was valid, and the sorting process has been initiated.
     * @apiResponse 400 Invalid request data - the input does not meet the validation criteria.
     */
    @PostMapping("/mergeSort")
    @Operation(summary = "Performs merge sort on an integer array",
            description = "Submits an integer array for sorting. Returns a response with the processing status.")
    @ApiResponse(responseCode = "200", description = "Successful processing",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MergeSortResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<MergeSortResponse> processMergeSort(@Valid @RequestBody MergeSortRequest request) throws JsonProcessingException {
        log.debug("Received merge sort request with data: {}", request.getArray().length);
        String requestId = UUID.randomUUID().toString();
        coordinatorService.startMergeSortProcess(requestId, request.getArray());
        log.info("Merge sort processing started successfully with id: " + requestId);
        return ResponseEntity.ok(new MergeSortResponse("Success", "Data is being processed.", requestId));
    }

}
