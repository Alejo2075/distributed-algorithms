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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<MergeSortResponse> processMergeSort(@Valid @RequestBody MergeSortRequest request) throws JsonProcessingException {
        log.debug("Received merge sort request with data: {}", request.getArray().length);
        String requestId = UUID.randomUUID().toString();
        coordinatorService.startMergeSortProcess(requestId, request.getArray());
        log.info("Merge sort processing started successfully with id: " + requestId);
        return ResponseEntity.ok(new MergeSortResponse("Success", "Data is being processed.", requestId));
    }

}
