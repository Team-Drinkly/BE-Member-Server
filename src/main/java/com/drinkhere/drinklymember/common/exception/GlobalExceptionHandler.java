package com.drinkhere.drinklymember.common.exception;

import com.drinkhere.drinklymember.common.response.ApplicationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * Controller Layer에서 발생하는 예외를 처리하는 것이지
 * Filter 단의 예외는 인식하지 못한다...!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApplicationResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ApplicationResponse.badRequest(null, e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ApplicationResponse<Void>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(404).body(ApplicationResponse.custom(null, 404, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApplicationResponse<Void>> handleGeneralException(Exception e) {
        return ResponseEntity.internalServerError().body(ApplicationResponse.server(null, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApplicationResponse<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(500).body(ApplicationResponse.server(null, e.getMessage()));
    }
}
