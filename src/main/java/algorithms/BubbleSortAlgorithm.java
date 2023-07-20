package algorithms;

import dtos.SortResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BubbleSortAlgorithm {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sort(int[] arr, int delay, String sessionId) throws InterruptedException {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
                messagingTemplate.convertAndSendToUser(sessionId, "topic/bubbleSort", new SortResponse());
                Thread.sleep(delay);
            }
        }
        messagingTemplate.convertAndSendToUser(sessionId, "topic/bubbleSort", arr);
    }
}
