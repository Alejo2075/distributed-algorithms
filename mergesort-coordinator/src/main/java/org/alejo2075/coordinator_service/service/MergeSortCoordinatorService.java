package org.alejo2075.coordinator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MergeSortCoordinatorService {

     void startMergeSortProcess(int[] array) throws JsonProcessingException;

}
