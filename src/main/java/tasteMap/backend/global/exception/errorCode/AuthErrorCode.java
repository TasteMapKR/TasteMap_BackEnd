package tasteMap.backend.global.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@AllArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode{
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST, "Refresh Token을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "Refresh Token을 찾을 수 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Refresh Token이 만료되었습니다.");
    private HttpStatus httpStatus;
    private String message;
}

