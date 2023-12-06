package plate.back.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation Exception 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidException(
            MethodArgumentNotValidException exception) {

        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return ResponseEntity.status(400).body(errors);
    }

    // Custom Exception 처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseDto> handleCustomException(CustomException exception) {

        ErrorResponseDto errorResponseDto = ErrorResponseDto.from(exception);

        log.error(exception.getMessage(), exception);
        
        return ResponseEntity.status(exception.getErrorCode().getStatus().value()).body(errorResponseDto);
    }

    // Undefined Exception 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUndefinedException(Exception exception) {

        ErrorResponseDto errorResponseDto = ErrorResponseDto.from(exception);

        log.error(exception.getMessage(), exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }
}