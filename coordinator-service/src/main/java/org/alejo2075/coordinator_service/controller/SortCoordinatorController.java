package org.alejo2075.coordinator_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.alejo2075.coordinator_service.dto.request.MergeSortRequest;
import org.alejo2075.coordinator_service.dto.response.MergeSortResponse;
import org.alejo2075.coordinator_service.service.CoordinatorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/algorithms/sort")
@Validated
public class SortCoordinatorController {

    private static final Logger logger = LogManager.getLogger(SortCoordinatorController.class);
    private final CoordinatorService coordinatorService;

    public SortCoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @PostMapping("/mergeSort")
    @Operation(summary = "Processes an integer array using the merge sort algorithm",
            description = "Submits an integer array for sorting. Returns a response with the processing status.")
    @ApiResponse(responseCode = "200", description = "Successful processing",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MergeSortResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<MergeSortResponse> processMergeSort(@Valid @RequestBody MergeSortRequest request) {
        logger.debug("Received merge sort request with data: {}", request.getArr().length);
        try {
            coordinatorService.coordinate(request.getArr());
            logger.info("Merge sort processing started successfully.");
            return ResponseEntity.ok(new MergeSortResponse("Success", "Data is being processed."));
        } catch (Exception e) {
            logger.error("Error processing merge sort request", e);
            return ResponseEntity.internalServerError().body(new MergeSortResponse("Failure", e.getMessage()));
        }
    }
}
