package com.disclose.paws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DiscloseApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DiscloseApiException(String message) {
        super(message);
    }

    public DiscloseApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiscloseApiException(Throwable cause) {
        super(cause);
    }
}
