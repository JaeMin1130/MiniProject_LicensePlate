package plate.back.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import plate.back.global.exception.CustomException;
import plate.back.global.exception.ErrorCode;
import plate.back.global.exception.ErrorResponseDto;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity<ErrorResponseDto> handleCustomException(CustomException exception) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .statusCode(exception.getErrorCode().getStatus().value())
                .message(exception.getErrorCode().getMessage())
                .build();
        return ResponseEntity.status(exception.getErrorCode().getStatus().value()).body(errorResponseDto);
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ErrorResponse> undefinedException(Exception exception)
    // {

    // ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
    // .statusCode(exception.hashCode())
    // .message(exception.getMessage())
    // .build();

    // log.error(exception.getMessage(), exception);

    // return ResponseEntity.badRequest().build();
    // }
}