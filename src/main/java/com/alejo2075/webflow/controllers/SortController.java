package com.alejo2075.webflow.controllers;

import com.alejo2075.webflow.algorithms.SortAlgorithms;
import com.alejo2075.webflow.dtos.SortRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class SortController {

    @Autowired
    private SortAlgorithms sortAlgorithms;

    @MessageMapping("/bubbleSort")
    public void bubbleSort(@Payload SortRequest request) throws InterruptedException {
        sortAlgorithms.bubbleSort(request.getArr(), request.getDelay());
    }

    @MessageMapping("/selectionSort")
    public void selectionSort(@Payload SortRequest request) throws InterruptedException {
        sortAlgorithms.selectionSort(request.getArr(), request.getDelay());
    }

    @MessageMapping("/insertionSort")
    public void insertionSort(@Payload SortRequest request) throws InterruptedException {
        sortAlgorithms.insertionSort(request.getArr(), request.getDelay());
    }

    @MessageMapping("/mergeSort")
    public void mergeSort(@Payload SortRequest request) throws InterruptedException {
        sortAlgorithms.mergeSort(request.getArr(), request.getDelay());
    }

    @MessageMapping("/quickSort")
    public void quickSort(@Payload SortRequest request) throws InterruptedException {
        sortAlgorithms.quickSort(request.getArr(), request.getDelay());
    }
}
