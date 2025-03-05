package com.oxyl.technight_api_first.config;

import com.oxyl.technight_api_first.server.model.ErrorDto;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handle(ResponseStatusException exception) {
        logger.error(exception.getReason());
        return switch (exception.getStatusCode()) {
            case BAD_REQUEST -> ResponseEntity.badRequest().body(toErrorDto(exception.getReason()));
            default -> ResponseEntity.internalServerError().body(toErrorDto(exception.getReason()));
        };
    }

    @ExceptionHandler({FeignException.class, IOException.class})
    public ResponseEntity<ErrorDto> handleClientException(Exception e) {
        logger.error("An client exception occurred {}", e.getMessage());
        return ResponseEntity.internalServerError().body(toErrorDto("Error trying to reach client api, try again"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handle(Exception e) {
        logger.error("An exception occurred {}", e.getMessage());
        return ResponseEntity.internalServerError().body(toErrorDto("An error occurred, contact the administrator"));
    }

    private ErrorDto toErrorDto(String message) {
        return new ErrorDto().message(message);
    }
}
