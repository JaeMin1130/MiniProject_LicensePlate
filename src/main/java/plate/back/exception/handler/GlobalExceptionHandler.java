package plate.back.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import plate.back.exception.CustomException;
import plate.back.exception.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ErrorResponseDto.builder()
                .statusCode(ex.getErrorCode().getStatus())
                .message(ex.getErrorCode().getMessage())
                .build(), HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<ErrorResponseDto> handleServerException(Exception ex) {
        return new ResponseEntity<>(ErrorResponseDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}