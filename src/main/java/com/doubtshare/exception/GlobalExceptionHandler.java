package com.doubtshare.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.doubtshare.dto.ApiError;
import com.doubtshare.entity.LoggerEntity;
import com.doubtshare.repository.LoggerRepository;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@Autowired
	LoggerRepository loggerRepository;
    // Validation errors (like @NotBlank)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            validationErrors.put(fieldName, message);
        });

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("One or more fields have invalid values.")
                .path(request.getDescription(false))
                .validationErrors(validationErrors)
                .build();
        
        	// Log the error to the database
                LoggerEntity loggerEntity = LoggerEntity.builder()
                                					.action("Validation Error")
                                					.timestamp(LocalDateTime.now().toString())
                                					.details("Validation error occurred")
                                					.status("Failed")
                                					.errorMessage(ex.getMessage())
                                					.build();
                loggerRepository.save(loggerEntity);
                
                log.info("Validation error logged: {}", loggerEntity);
                        
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex, WebRequest request) {
        String errorMessage = ex.getMessage(); // This will be "Tutor not found" etc.
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(errorMessage)
                .path(request.getDescription(false))
                .build();
        LoggerEntity loggerEntity = LoggerEntity.builder()
                .action("Runtime Exception")
                .timestamp(LocalDateTime.now().toString())
                .details("Error during tutor ping: " + request.getDescription(false))
                .status("FAILED")
                .errorMessage(errorMessage)
                .build();
        loggerRepository.save(loggerEntity); // Now each unique RuntimeException is logged
        log.warn("RuntimeException logged: {}", loggerEntity);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
  }

