package tasteMap.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tasteMap.backend.global.exception.errorCode.ErrorCode;

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
}