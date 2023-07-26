package com.alejo2075.webflow.controllers;

import com.alejo2075.webflow.algorithms.LinearSearchAlgorithm;
import com.alejo2075.webflow.dtos.SearchRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class LinearSearchController {

    private LinearSearchAlgorithm linearSearchAlgorithm;

    @MessageMapping("/linearSearch")
    public void search(@Payload SearchRequest request) throws InterruptedException {
        linearSearchAlgorithm.search(request.getArr(), request.getTarget(), request.getDelay());
    }
}
