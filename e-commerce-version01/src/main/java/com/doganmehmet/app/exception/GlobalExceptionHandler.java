package com.doganmehmet.app.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final DateTimeFormatter m_formatter;

    public GlobalExceptionHandler(DateTimeFormatter formatter)
    {
        m_formatter = formatter;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException apiException, WebRequest request)
    {
        var errorResponse = new HashMap<String, String>();
        errorResponse.put("errorCode", apiException.getMyError().getErrorCode());
        errorResponse.put("message", apiException.getMessage());
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        errorResponse.put("errorTime", LocalDateTime.now().format(m_formatter));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception, WebRequest request)
    {
        var errorResponse = new HashMap<String, String>();
        var errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        errorResponse.put("errorCode", "VALIDATION_ERROR");
        errorResponse.put("message", errorMessages);
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        errorResponse.put("errorTime", LocalDateTime.now().format(m_formatter));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException exception, WebRequest request)
//    {
//        var errorResponse = new HashMap<String, String>();
//        errorResponse.put("errorCode", "JWT_TOKEN_EXPIRED");
//        errorResponse.put("message", exception.getMessage());
//        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
//        errorResponse.put("errorTime", LocalDateTime.now().format(m_formatter));
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAuthorizationDeniedException(AuthorizationDeniedException exception, WebRequest request)
    {
        var errorResponse = new HashMap<String, String>();
        errorResponse.put("errorCode", "ACCESS_DENIED");
        errorResponse.put("message", "You do not have permission to access this resource: " + exception.getMessage());
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        errorResponse.put("errorTime", LocalDateTime.now().format(m_formatter));
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception exception, WebRequest request)
    {
        var errorResponse = new HashMap<String, String>();
        errorResponse.put("errorCode", exception.getClass().getName());
        errorResponse.put("message", "An unexpected error occurred: " + exception.getMessage());
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
        errorResponse.put("errorTime", LocalDateTime.now().format(m_formatter));

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
