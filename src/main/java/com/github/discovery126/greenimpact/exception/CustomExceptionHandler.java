package com.github.discovery126.greenimpact.exception;

import com.github.discovery126.greenimpact.dto.ErrorDto;
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

    @ExceptionHandler({UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class,
            TaskAlreadyTakenException.class,
            RewardOutOfStockException.class,
            UserAlreadyRegisteredEventException.class,
            UserAlreadyConfirmException.class})
    protected ResponseEntity<ErrorDto> handleExistsException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorDto(List.of(ex.getMessage())));
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorDto> handleUnauthorizedException(UnauthorizedException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(List.of(ex.getMessage())));
    }

    @ExceptionHandler({RoleNotFoundException.class,
            CityNotFoundException.class,
            UserNotFoundException.class,
            NotFoundOpenCageException.class,
            EventNotFoundException.class,
            RewardCategoryNotFoundException.class,
            RewardNotFoundException.class,
            TaskCategoryNotFoundException.class,
            TaskNotFoundException.class,
            PhotoNotFoundException.class,
            BadEventCodeException.class,
            TaskAlreadyAnsweredException.class,
            BadCommentException.class,
            NotEnoughPointsException.class})
    protected ResponseEntity<ErrorDto> handleNotFoundException(RuntimeException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(List.of(ex.getMessage())));
    }

    @ExceptionHandler({NotFoundOpenCageApiException.class,
            FileStorageException .class})
    protected ResponseEntity<ErrorDto> handleNotFoundOpenCageApiException(NotFoundOpenCageApiException ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(List.of(ex.getMessage())));
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

        return ResponseEntity.badRequest()
                .body(new ErrorDto(errors));

    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(Collections.singletonList("Неправильный email или пароль")));
    }

}