//package com.example.RateLimitedURLShortener.Exception;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(err -> {
//            String field = ((FieldError)err).getField();
//            String msg   = err.getDefaultMessage();
//            errors.put(field, msg);
//        });
//        return new ResponseEntity<>(Map.of(
//                "status", HttpStatus.BAD_REQUEST.value(),
//                "errors", errors
//        ), HttpStatus.BAD_REQUEST);
//    }
//
//    // 404 – Missing short-code
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex) {
//        return new ResponseEntity<>(Map.of(
//                "status", HttpStatus.NOT_FOUND.value(),
//                "error", ex.getMessage()
//        ), HttpStatus.NOT_FOUND);
//    }
//
//    // 429 – Rate limit exceeded
//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Object> handleRateLimit(ResponseStatusException ex) {
//        return new ResponseEntity<>(Map.of(
//                "status", HttpStatus.TOO_MANY_REQUESTS.value(),
//                "error", ex.getMessage()
//        ), HttpStatus.TOO_MANY_REQUESTS);
//    }
//
//    // 500 – Fallback for everything else
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleGeneral(Exception ex) {
//        return new ResponseEntity<>(Map.of(
//                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "error", "An unexpected error occurred"
//        ), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
