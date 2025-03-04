package com.oxyl.technight_api_first.config;

import com.oxyl.technight_api_first.server.model.ErrorDto;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({FeignException.class, IOException.class})
    public ResponseEntity<ErrorDto> handleClientException() {
        return ResponseEntity.internalServerError().body(toErrorDto("Error trying to reach client api, try again"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handle() {
        return ResponseEntity.badRequest().body(toErrorDto("An error occurred, contact the administrator"));
    }

    private ErrorDto toErrorDto(String message) {
        return new ErrorDto().message(message);
    }
}
