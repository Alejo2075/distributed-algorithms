package org.alejo2075.coordinator_service;

import org.alejo2075.coordinator_service.service.MergeSortCoordinatorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Random;

@SpringBootApplication
public class MergeSortCoordinatorServiceApplication {

    private static final int MAX_ARRAY_SIZE = 100;

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(MergeSortCoordinatorServiceApplication.class, args);

        System.out.println("Press any key to start the MergeSort process...");
        System.in.read();

        int[] array = generateRandomArray(MAX_ARRAY_SIZE);

        MergeSortCoordinatorService coordinator = context.getBean(MergeSortCoordinatorService.class);

        coordinator.startMergeSortProcess(array);
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