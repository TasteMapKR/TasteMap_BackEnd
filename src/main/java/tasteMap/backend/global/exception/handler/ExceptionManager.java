package tasteMap.backend.global.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tasteMap.backend.global.exception.AppException;
import tasteMap.backend.global.exception.errorCode.ErrorCode;
import tasteMap.backend.global.response.ResponseDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseDto<?>> appExceptionHandler(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ResponseDto<?> response = ResponseDto.fail(errorCode.getHttpStatus().value(), errorCode.getMessage() + " " + e.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 모든 유효성 검사 오류를 수집하여 errors 맵에 추가
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 커스텀 응답 객체 생성
        ResponseDto<Map<String, String>> response = ResponseDto.fail(
            422,  // 422 Unprocessable Entity 상태 코드
            "유효성 검사에 실패했습니다.",
            errors
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
}
