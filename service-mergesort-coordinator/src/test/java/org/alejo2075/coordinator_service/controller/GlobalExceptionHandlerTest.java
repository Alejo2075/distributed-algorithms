package org.alejo2075.coordinator_service.controller;

import org.alejo2075.coordinator_service.model.dto.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.KafkaException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        webRequest = new ServletWebRequest(request);
    }

    @Test
    void handleException_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Internal server error occurred");
        ResponseEntity<Object> response = exceptionHandler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", ((ErrorResponse) response.getBody()).getStatus());
        assertEquals("Internal server error occurred", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    void handleHttpRequestMethodNotSupportedException_ShouldReturnMethodNotAllowed() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<Object> response = exceptionHandler.handleHttpRequestMethodNotSupportedException(exception);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Method Not Allowed", ((ErrorResponse) response.getBody()).getStatus());
        assertEquals("This method is not supported for this endpoint: POST", ((ErrorResponse) response.getBody()).getMessage());
    }


    @Test
    void handleKafkaException_ShouldReturnServiceUnavailable() {
        KafkaException exception = new KafkaException("Failed to send message");

        ResponseEntity<Object> response = exceptionHandler.handleKafkaException(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Service Unavailable", ((ErrorResponse) response.getBody()).getStatus());
        assertEquals("Failed to communicate with Kafka: Failed to send message", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    void handleDataAccessException_ShouldReturnServiceUnavailable() {
        DataAccessException exception = new DataAccessException("Database error occurred") {};

        ResponseEntity<Object> response = exceptionHandler.handleDataAccessException(exception);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Service Unavailable", ((ErrorResponse) response.getBody()).getStatus());
        assertEquals("Database access issue: Database error occurred", ((ErrorResponse) response.getBody()).getMessage());
    }



}

