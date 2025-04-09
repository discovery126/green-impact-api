package com.github.discovery126.greenimpact.exception;

import com.github.discovery126.greenimpact.dto.response.BaseErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseErrorResponse> handleCustomException(CustomException ex) {

        return ResponseEntity
                .badRequest()
                .body(new BaseErrorResponse(HttpStatus.BAD_REQUEST.value(),
                        Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<BaseErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {

        return ResponseEntity
                .badRequest()
                .body(new BaseErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                        Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(FileStorageException.class)
    protected ResponseEntity<BaseErrorResponse> handleNotFoundOpenCageApiException(NotFoundOpenCageApiException ex) {

        return ResponseEntity
                .badRequest()
                .body(new BaseErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                        Collections.singletonList(ex.getMessage())));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        final List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ResponseEntity
                .badRequest()
                .body(new BaseErrorResponse(HttpStatus.BAD_REQUEST.value(),errors));


    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .badRequest()
                .body(new BaseErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                        Collections.singletonList(ValidationConstants.BAD_CREDENTIALS)));
    }

}