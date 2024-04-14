package org.alejo2075.coordinator_service.controller;

import org.alejo2075.coordinator_service.dto.request.MergeSortRequest;
import org.alejo2075.coordinator_service.dto.response.MergeSortResponse;
import org.alejo2075.coordinator_service.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SortCoordinatorController {

    @Autowired
    private CoordinatorService coordinatorService;
    
    @PostMapping("/large")
    public MergeSortResponse handleLargeArray(@RequestBody MergeSortRequest request) {
        
        MergeSortResponse response = null;
        return response;
    }

    @PostMapping("/upload")
    public MergeSortResponse uploadFile(@RequestParam("file") MultipartFile file) {
        MergeSortResponse response = null;
        return response;
    }
    

    @MessageMapping("/sendArray")
    @SendTo("/topic/array")
    public String handleArray(int[] array) {
        return "Received array with length: " + array.length;
    }
}
