package com.neurocom.safe_card.exception;

import com.neurocom.safe_card.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.time.Instant;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest mockRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        mockRequest = mock(WebRequest.class);
    }

    @Test
    void handleInvalidPan_returnsBadRequest() {
        InvalidPanException ex = new InvalidPanException("PAN is invalid");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidPan(ex, mockRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Invalid PAN");
        assertThat(response.getBody().message()).isEqualTo("PAN is invalid");
        assertThat(response.getBody().timestamp()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void handleInvalidRequest_returnsBadRequest() {
        InvalidRequestException ex = new InvalidRequestException("Missing name");

        ResponseEntity<ErrorResponse> response = handler.handleEncryptionErrors(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().error()).isEqualTo("Missing or Invalid Request Parameter");
        assertThat(response.getBody().message()).isEqualTo("Missing name");
    }

    @Test
    void handleNotFound_returnsNotFound() {
        NotFoundException ex = new NotFoundException("Card not found");

        ResponseEntity<ErrorResponse> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().error()).isEqualTo("PAN Not Found");
        assertThat(response.getBody().message()).isEqualTo("Card not found");
    }

    @Test
    void handleEncryptionException_returnsInternalServerError() {
        EncryptionException ex = new EncryptionException("AES key missing", null);

        ResponseEntity<ErrorResponse> response = handler.handleEncryptionErrors(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().error()).isEqualTo("Encryption Failure");
        assertThat(response.getBody().message()).isEqualTo("AES key missing");
    }

    @Test
    void handleGeneral_returnsInternalServerError() {
        Exception ex = new RuntimeException("Something went wrong");

        ResponseEntity<ErrorResponse> response = handler.handleGeneral(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().error()).isEqualTo("Internal Error");
        assertThat(response.getBody().message()).isEqualTo("Something went wrong");
    }
}
