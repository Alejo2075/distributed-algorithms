package org.alejo2075.coordinator_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CoordinatorService {

    public void startMergeSortProcess(String requestId, int[] array) throws JsonProcessingException;

}
