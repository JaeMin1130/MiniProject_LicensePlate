package plate.back.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponseDto {
    private Integer statusCode;
    private String message;
}
