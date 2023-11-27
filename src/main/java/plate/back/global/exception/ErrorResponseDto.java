package plate.back.global.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponseDto {
    private String message;

    public static ErrorResponseDto of(CustomException e){
        return ErrorResponseDto.builder().message(e.getErrorCode().getMessage()).build();
    }
}
