package org.alejo2075.coordinator_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.dto.request.BinarySearchRequest;
import org.alejo2075.coordinator_service.model.dto.request.MergeSortRequest;
import org.alejo2075.coordinator_service.model.dto.response.BinarySearchResponse;
import org.alejo2075.coordinator_service.model.dto.response.MergeSortResponse;
import org.alejo2075.coordinator_service.service.CoordinatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/algorithms")
@Log4j2
public class CoordinatorController {

    private final CoordinatorService coordinatorService;

    public CoordinatorController(CoordinatorService coordinatorService) {
        this.coordinatorService = coordinatorService;
    }

    @PostMapping("/mergeSort")
    @Operation(summary = "Performs merge sort on an integer array",
            description = "Submits an integer array for sorting. Returns a response with the processing status.")
    @ApiResponse(responseCode = "200", description = "Successful processing",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MergeSortResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<MergeSortResponse> processMergeSort(@Valid @RequestBody MergeSortRequest request) {
        log.debug("Received merge sort request with data: {}", request.getArray().length);
        coordinatorService.startMergeSortProcess(request.getArray());
        log.info("Merge sort processing started successfully.");
        return ResponseEntity.ok(new MergeSortResponse("Success", "Data is being processed."));
    }

    @PostMapping("/binarySearch")
    @Operation(summary = "Performs binary search on a sorted integer array",
            description = "Submits a sorted integer array and a target for searching. Returns a response with the processing status.")
    @ApiResponse(responseCode = "200", description = "Successful processing",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BinarySearchResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<BinarySearchResponse> processBinarySearch(@Valid @RequestBody BinarySearchRequest request) {
        log.debug("Received binary search request for element {} in array of length {}", request.getTarget(), request.getSortedArray().length);
        coordinatorService.startBinarySearchProcess(request.getSortedArray(), request.getTarget());
        log.info("Binary search processing started successfully.");
        return ResponseEntity.ok(new BinarySearchResponse("Success", "Data is being processed."));
    }

}
