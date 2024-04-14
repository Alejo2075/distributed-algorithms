package org.alejo2075.coordinator_service.service;

import org.alejo2075.coordinator_service.algorithm.simple.MergeSortSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CoordinatorServiceImpl implements CoordinatorService{

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void coordinate(int[] arr) {

    }
}
