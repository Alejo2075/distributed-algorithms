package org.alejo2075.coordinator_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.alejo2075.coordinator_service.service.MergeSortCoordinatorServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Random;

@SpringBootApplication
public class MergeSortCoordinatorServiceApplication {

    public static void main(String[] args) throws JsonProcessingException {
        ConfigurableApplicationContext context = SpringApplication.run(MergeSortCoordinatorServiceApplication.class, args);

        int[] array = generateRandomArray(10000);

        MergeSortCoordinatorServiceImpl mergeSortCoordinatorServiceImpl = context.getBean(MergeSortCoordinatorServiceImpl.class);


        mergeSortCoordinatorServiceImpl.startMergeSortProcess(array);
    }


    private static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(1000);
        }
        return array;
    }
}
