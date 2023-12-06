package plate.back.global.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponseDto {
    private String message;

    public static ErrorResponseDto from(CustomException e){
        return ErrorResponseDto.builder().message(e.getErrorCode().getMessage()).build();
    }

    public static ErrorResponseDto from(Exception e){
        return ErrorResponseDto.builder().message(e.getMessage()).build();
    }
}
