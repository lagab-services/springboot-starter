package com.lagab.blank.common.web.error.handler;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.lagab.blank.common.exception.BadRequestException;
import com.lagab.blank.common.exception.NotFoundException;
import com.lagab.blank.common.web.error.http.ResponseError;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleControllerException(Throwable throwable) {
        if (throwable instanceof NoHandlerFoundException) {
            return buildResponse(HttpStatus.NOT_FOUND, "The requested url does not exist");
        } else if (throwable instanceof CannotCreateTransactionException) {
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to acquire JDBC connection");
        } else if (throwable instanceof AccessDeniedException) {
            return buildResponse(HttpStatus.FORBIDDEN, "You are not authorized to access this resource.");
        } else if (throwable instanceof NotFoundException) {
            return buildResponse(HttpStatus.NOT_FOUND, throwable.getMessage());
        } else if (throwable instanceof BadRequestException) {
            return buildResponse(HttpStatus.BAD_REQUEST, throwable.getMessage());
        } else if (throwable instanceof BadCredentialsException) {
            return buildResponse(HttpStatus.UNAUTHORIZED, "The username or password is incorrect");
            //        } else if (throwable instanceof ResponseErrorException exception) {
            //            return exception.getResponseError().toResponseEntity();
        } else if (throwable instanceof AccountStatusException) {
            return buildResponse(HttpStatus.FORBIDDEN, "The account is locked");
        } else if (throwable instanceof SignatureException) {
            return buildResponse(HttpStatus.FORBIDDEN, "The JWT signature is invalid.");
        } else if (throwable instanceof ExpiredJwtException) {
            return buildResponse(HttpStatus.FORBIDDEN, "The JWT token has expired.");
        } else if (throwable instanceof MethodArgumentNotValidException ex) {
            List<String> errorsMessage = ex.getBindingResult().getAllErrors()
                                           .stream()
                                           .map(DefaultMessageSourceResolvable::getCode)
                                           .toList();
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            if (!errors.isEmpty()) {
                return buildResponse(HttpStatus.BAD_REQUEST, errors);
            }
            return buildResponse(HttpStatus.BAD_REQUEST, errorsMessage);
        } else {
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, throwable.getMessage());
        }
    }

    private ResponseEntity buildResponse(HttpStatus status, String errorDescription) {
        return ResponseError.builder()
                            .error(status.getReasonPhrase())
                            .status(status)
                            .code(String.valueOf(status.value()))
                            .errorDescription(errorDescription)
                            .build()
                            .toResponseEntity();
    }

    private ResponseEntity buildResponse(HttpStatus status, List<String> errorDescriptions) {
        return ResponseError.builder()
                            .error(status.getReasonPhrase())
                            .status(status)
                            .code(String.valueOf(status.value()))
                            .errorDescriptions(errorDescriptions)
                            .build()
                            .toResponseEntity();
    }

    private ResponseEntity buildResponse(HttpStatus status, Map<String, String> errorDescriptionMap) {
        return ResponseError.builder()
                            .error(status.getReasonPhrase())
                            .status(status)
                            .code(String.valueOf(status.value()))
                            .errorDescriptionMap(errorDescriptionMap)
                            .build()
                            .toResponseEntity();
    }
}
