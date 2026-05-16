package com.appsec.lab.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class HttpResponseException extends RuntimeException {

    private final HttpStatus status;

    public HttpResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
