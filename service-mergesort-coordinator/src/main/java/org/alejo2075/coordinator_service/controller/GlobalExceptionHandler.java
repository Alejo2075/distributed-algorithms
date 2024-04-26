package org.alejo2075.coordinator_service.controller;

import lombok.extern.log4j.Log4j2;
import org.alejo2075.coordinator_service.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.kafka.KafkaException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

/**
 * Global exception handler for the coordinator service.
 * This class provides centralized exception handling across all {@code @RequestMapping} methods.
 * It translates exceptions into HTTP responses to maintain a consistent error structure returned to clients.
 *
 * @author org.apache.logging.log4j
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    /**
     * Handles generic exceptions and returns an internal server error response.
     *
     * @param e the exception that was thrown.
     * @return a {@code ResponseEntity} object containing the error details.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("Internal Server Error", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse("Failure", e.getMessage()));
    }

    /**
     * Handles validation errors thrown when method arguments are not valid.
     * It captures and logs the error details, and then constructs a user-friendly error response.
     *
     * @param e the exception that encapsulates the validation errors.
     * @return a {@code ResponseEntity} with detailed validation error information.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error", e);
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", errorMsg));
    }

    /**
     * Handles exceptions specifically related to Kafka communication failures.
     *
     * @param e the Kafka-specific exception.
     * @return a {@code ResponseEntity} indicating that the service is unavailable.
     */
    @ExceptionHandler(KafkaException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<Object> handleKafkaException(KafkaException e) {
        log.error("Kafka communication error", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse("Service Unavailable", "Failed to communicate with Kafka: " + e.getMessage()));
    }

    /**
     * Handles database access errors and converts them into a standard response structure.
     *
     * @param e the exception related to database operations.
     * @return a {@code ResponseEntity} indicating that the database service is unavailable.
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException e) {
        log.error("Database access error", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse("Service Unavailable", "Database access issue: " + e.getMessage()));
    }

    /**
     * Handles HTTP method not supported exceptions and provides a clear response to the client.
     *
     * @param e the exception that indicates the use of an unsupported HTTP method.
     * @return a {@code ResponseEntity} indicating that the method is not allowed.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Method Not Supported", e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse("Method Not Allowed", "This method is not supported for this endpoint: " + e.getMethod()));
    }
}
