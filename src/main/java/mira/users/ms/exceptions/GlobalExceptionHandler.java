package mira.users.ms.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> catchInvalidInputDataException(MethodArgumentNotValidException e) {
        List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(violations, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> catchInvalidInputDataException(ConstraintViolationException e) {
        List<Violation> violations = e.getConstraintViolations().stream()
                .map(error -> new Violation(error.getPropertyPath().toString(), error.getMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(violations, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> notFoundException(NotFoundException notFoundException){
        log.error("Resource not found: {}", notFoundException.getMessage());
        return new ResponseEntity<>(notFoundException.getMessage(),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<?> badRequestException(BadRequestException e){
        log.error("Bad request, {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<?> anyException(Exception e){
        log.error("Exception, {}", e.getMessage() + "\n" + e.getStackTrace());
        return new ResponseEntity<>(e.getMessage() + "\n" + e.getStackTrace()[0],
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
