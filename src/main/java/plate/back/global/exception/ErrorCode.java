package plate.back.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // GlobalException

    // Member Exception
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "이미 가입된 아이디 입니다"),
    Member_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Record Exception
    RECORD_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 기록이 존재하지 않습니다."),

    // Token Exception
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토근이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "유효한 토큰 형식(Bearer ~)이 아닙니다.");

    // Flask Exception

    // S3 Exception
    
    private final HttpStatus status;
    private final String message;
}