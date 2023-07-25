package com.alejo2075.webflow.controllers;

import com.alejo2075.webflow.algorithms.BubbleSortAlgorithm;
import com.alejo2075.webflow.dtos.SortRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class BubbleSortController {

    @Autowired
    private BubbleSortAlgorithm bubbleSortAlgorithm;

    @MessageMapping("/bubbleSort")
    public void sort(@Payload SortRequest request) throws InterruptedException {
        bubbleSortAlgorithm.sort(request.getArr(), request.getDelay());
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
