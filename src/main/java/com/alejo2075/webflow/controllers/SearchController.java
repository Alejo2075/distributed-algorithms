package com.alejo2075.webflow.controllers;

import com.alejo2075.webflow.algorithms.SearchAlgorithms;
import com.alejo2075.webflow.dtos.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class SearchController {

    @Autowired
    private SearchAlgorithms searchAlgorithms;

    @MessageMapping("/binarySearch")
    public void binarySearch(@Payload SearchRequest request) throws InterruptedException {
        searchAlgorithms.binarySearch(request.getArr(), request.getTarget(), request.getDelay());
    }

    @MessageMapping("/linearSearch")
    public void linearSearch(@Payload SearchRequest request) throws InterruptedException {
        searchAlgorithms.linearSearch(request.getArr(), request.getTarget(), request.getDelay());
    }
}
