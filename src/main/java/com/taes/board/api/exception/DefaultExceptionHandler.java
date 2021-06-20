package com.taes.board.api.exception;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@Log4j2
@ControllerAdvice
public class DefaultExceptionHandler
{
    // =================================================================================================================
    // DataAccessException
    // =================================================================================================================

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e)
    {
        String errorId = UUID.randomUUID().toString();

        log.error("DefaultExceptionHandler.handleDataAccessException: ERROR ID = {}", errorId, e);
        String errorMessage = String.format("Ineternal server error [%s]", errorId);

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // =================================================================================================================
    // ApiException
    // =================================================================================================================

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleApiException(Exception e)
    {
        log.error("DefaultExceptionHandler.handleApiException: ", e);
        String errorMessage = e.getMessage();

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // =================================================================================================================
    // Exception
    // =================================================================================================================

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(Exception e)
    {
        log.error("DefaultExceptionHandler.handleAccessDeniedException: ", e);
        String errorMessage = e.getMessage();

        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    // =================================================================================================================
    // Exception
    // =================================================================================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleDefaultException(Exception e)
    {
        log.error("DefaultExceptionHandler.handleDefaultException: ", e);
        String errorMessage = e.getMessage();

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
