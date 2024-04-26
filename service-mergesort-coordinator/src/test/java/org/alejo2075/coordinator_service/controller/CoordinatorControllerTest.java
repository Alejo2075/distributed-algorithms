package org.alejo2075.coordinator_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alejo2075.coordinator_service.model.dto.request.BinarySearchRequest;
import org.alejo2075.coordinator_service.model.dto.request.MergeSortRequest;
import org.alejo2075.coordinator_service.service.CoordinatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class CoordinatorControllerTest {

    @Mock
    private CoordinatorService coordinatorService;

    @InjectMocks
    private CoordinatorController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testProcessMergeSort_Success() throws Exception {
        MergeSortRequest request = new MergeSortRequest(new int[]{5, 2, 9, 1, 5});
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/algorithms/mergeSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Data is being processed."));

        verify(coordinatorService).startMergeSortProcess(any());
    }

    @Test
    public void testProcessMergeSort_InvalidRequest() throws Exception {
        MergeSortRequest request = new MergeSortRequest();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/algorithms/mergeSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(coordinatorService, never()).startMergeSortProcess(any());
    }

    @Test
    public void testProcessMergeSort_InternalServerError() throws Exception {
        MergeSortRequest request = new MergeSortRequest(new int[]{5, 2, 9, 1, 5});
        String jsonRequest = objectMapper.writeValueAsString(request);

        doThrow(new RuntimeException("Internal Server Error")).when(coordinatorService).startMergeSortProcess(any());

        mockMvc.perform(post("/v1/algorithms/mergeSort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Failure"))
                .andExpect(jsonPath("$.message").value("Internal Server Error"));

        verify(coordinatorService).startMergeSortProcess(any());
    }

    @Test
    public void testProcessBinarySearch_Success() throws Exception {
        BinarySearchRequest request = new BinarySearchRequest(new int[]{1, 2, 3, 4, 5}, 3);
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/algorithms/binarySearch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Success"))
                .andExpect(jsonPath("$.message").value("Data is being processed."));

        verify(coordinatorService).startBinarySearchProcess(any(), anyInt());
    }

    @Test
    public void testProcessBinarySearch_InvalidRequest() throws Exception {
        BinarySearchRequest request = new BinarySearchRequest();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/algorithms/binarySearch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(coordinatorService, never()).startBinarySearchProcess(any(int[].class), anyInt());
    }

    @Test
    public void testProcessBinarySearch_InternalServerError() throws Exception {
        BinarySearchRequest request = new BinarySearchRequest(new int[]{1, 2, 3, 4, 5}, 3);
        String jsonRequest = objectMapper.writeValueAsString(request);

        doThrow(new RuntimeException("Internal Server Error")).when(coordinatorService).startBinarySearchProcess(any(int[].class), anyInt());

        mockMvc.perform(post("/v1/algorithms/binarySearch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Failure"))
                .andExpect(jsonPath("$.message").value("Internal Server Error"));

        verify(coordinatorService).startBinarySearchProcess(any(int[].class), anyInt());
    }


}
