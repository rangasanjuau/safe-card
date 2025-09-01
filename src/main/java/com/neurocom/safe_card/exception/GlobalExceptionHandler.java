package com.neurocom.safe_card.exception;

import com.neurocom.safe_card.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;


/**
 * Global exception handler to manage and format error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles InvalidPanException and returns a structured error response.
     *
     * @param ex the InvalidPanException instance
     * @return ResponseEntity with error details and HTTP status 400
     */
    @ExceptionHandler(InvalidPanException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPan(InvalidPanException ex, WebRequest request) {
        var error = new ErrorResponse(
                "Invalid PAN",
                ex.getMessage(),
                Instant.now()
        );
        log.error("InvalidPanException : ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles InvalidRequestException and returns a structured error response.
     *
     * @param ex the InvalidRequestException instance
     * @return ResponseEntity with error details and HTTP status 400
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleEncryptionErrors(InvalidRequestException ex) {
        var error = new ErrorResponse(
                "Missing or Invalid Request Parameter",
                ex.getMessage(),
                Instant.now()
        );
        log.error("InvalidRequestException : ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    /**
     * Handles NotFoundException and returns a structured error response.
     *
     * @param ex the NotFoundException instance
     * @return ResponseEntity with error details and HTTP status 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        var error = new ErrorResponse(
                "PAN Not Found",
                ex.getMessage(),
                Instant.now()
        );
        log.error("NotFoundException : ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles EncryptionException and returns a structured error response.
     *
     * @param ex the EncryptionException instance
     * @return ResponseEntity with error details and HTTP status 400
     */
    @ExceptionHandler(EncryptionException.class)
    public ResponseEntity<ErrorResponse> handleEncryptionErrors(EncryptionException ex) {
        var error = new ErrorResponse(
                "Encryption Failure",
                ex.getMessage(),
                Instant.now()
        );
        log.error("EncryptionException : ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


    /**
     * Handles all other exceptions and returns a structured error response.
     *
     * @param ex the Exception instance
     * @return ResponseEntity with error details and HTTP status 500
     */
    @ExceptionHandler(Exception.class) // fallback for all other exceptions
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        var error = new ErrorResponse(
                "Internal Error",
                ex.getMessage(),
                Instant.now()
        );
        log.error("Exception : ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }


}