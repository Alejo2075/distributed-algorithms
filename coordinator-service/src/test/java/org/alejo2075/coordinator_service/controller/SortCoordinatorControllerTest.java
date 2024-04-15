package org.alejo2075.coordinator_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.alejo2075.coordinator_service.dto.request.MergeSortRequest;
import org.alejo2075.coordinator_service.service.CoordinatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class SortCoordinatorControllerTest {

    @Mock
    private CoordinatorService coordinatorService;

    @InjectMocks
    private SortCoordinatorController controller;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void testProcessMergeSort_Success() throws Exception {
        MergeSortRequest request = new MergeSortRequest(new int[]{5, 2, 9, 1, 5});
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/algorithms/sort/mergeSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Data is being processed."));

        verify(coordinatorService, times(1)).coordinate(request.getArr());
    }

    @Test
    public void testProcessMergeSort_Failure() throws Exception {
        MergeSortRequest request = new MergeSortRequest(new int[]{5, 2, 9, 1, 5});
        String jsonRequest = objectMapper.writeValueAsString(request);

        doThrow(new RuntimeException("Internal error")).when(coordinatorService).coordinate(any(int[].class));

        mockMvc.perform(post("/v1/algorithms/sort/mergeSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Failure"))
                .andExpect(jsonPath("$.message").value("Internal error"));

        verify(coordinatorService, times(1)).coordinate(request.getArr());
    }


    @Test
    public void testProcessMergeSort_LargeArray() throws Exception {
        int[] largeArray = new int[10000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = (int) (Math.random() * 10000);
        }
        MergeSortRequest request = new MergeSortRequest(largeArray);
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/algorithms/sort/mergeSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Data is being processed."));

        verify(coordinatorService, times(1)).coordinate(request.getArr());
    }


    @Test
    public void testProcessMergeSort_EmptyArray() throws Exception {
        MergeSortRequest request = new MergeSortRequest(new int[]{});
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/algorithms/sort/mergeSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(coordinatorService, never()).coordinate(request.getArr());
    }

}
