package controllers;

import algorithms.BubbleSortAlgorithm;
import dtos.SortRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;

@Controller
public class BubbleSortController {

    @Autowired
    private BubbleSortAlgorithm bubbleSortAlgorithm;

    @MessageMapping("/bubbleSort")
    public void sort(SortRequest request, SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
        bubbleSortAlgorithm.sort(request.getArr(), request.getDelay(), headerAccessor.getSessionId());
    }


}
